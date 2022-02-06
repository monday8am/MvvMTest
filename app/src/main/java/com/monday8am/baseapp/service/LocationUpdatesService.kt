package com.monday8am.baseapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.monday8am.baseapp.MainActivity
import com.monday8am.baseapp.R
import com.monday8am.baseapp.domain.model.Coordinates
import com.monday8am.baseapp.domain.repo.LocationRepository
import com.monday8am.baseapp.domain.repo.PreferencesRepository
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.DateFormat
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class LocationUpdatesService : Service() {

    private val channelId = "channel_01"
    private val localBinder = LocalBinder()

    private var lastDistance = 0
    private var lastCoordinates: Coordinates = Coordinates(-90.0, 0.0)
    private val updateIntervalInMs: Long = 10000
    private val fastestUpdateIntervalInMs = updateIntervalInMs / 2
    private val notificationId = 12345678
    private var configurationChange = false
    private var serviceRunningInForeground = false

    private lateinit var notificationManager: NotificationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    @Inject
    lateinit var locationRepository: LocationRepository

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                with(locationResult) {
                    val newCoordinates = Coordinates(
                        longitude = lastLocation.longitude,
                        latitude = lastLocation.latitude
                    )
                    if (newCoordinates.isOutsideOf(lastCoordinates, maxRadioMeters)) {
                        lastDistance = newCoordinates.distanceTo(lastCoordinates)
                        locationRepository.addLocation(newCoordinates)
                        lastCoordinates = newCoordinates
                    }
                }
            }
        }

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val mChannel =
                NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Timber.d("Service started")

        val cancelFromNotification = intent.getBooleanExtra(extraStartedFromNotification, false)
        if (cancelFromNotification) {
            stopLocationUpdates()
            stopSelf()
        }
        return START_NOT_STICKY
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configurationChange = true
    }

    override fun onBind(intent: Intent): IBinder {
        Timber.d("in onBind()")
        stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
        return localBinder
    }

    override fun onRebind(intent: Intent) {
        Timber.d("in onRebind()")
        stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean {
        Timber.d("Last client unbound from service")

        // Called when the last client (MainActivity in case of this sample) unbinds from this
        // service. If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.
        if (!configurationChange && preferencesRepository.isLocationRequested) {
            Timber.d("Starting foreground service")
            startForeground(notificationId, getNotification())
        }
        return true
    }

    override fun onDestroy() {
        Timber.d("on destroy service!")
        preferencesRepository.isLocationRequested = false
    }

    //  Add location updates

    fun startLocationUpdates() {
        Timber.d("Requesting location updates")
        startService(Intent(applicationContext, LocationUpdatesService::class.java))
        try {
            val request = LocationRequest.create().apply {
                interval = updateIntervalInMs
                fastestInterval = fastestUpdateIntervalInMs
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            fusedLocationClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )
            preferencesRepository.isLocationRequested = true
        } catch (unlikely: SecurityException) {
            Timber.e("Lost location permission. Could not request updates. $unlikely")
        }
    }

    //  Removes location updates

    fun stopLocationUpdates() {
        Timber.d("Removing location updates")
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            stopSelf()
            preferencesRepository.isLocationRequested = false
        } catch (unlikely: SecurityException) {
            Timber.e("Lost location permission. Could not remove updates. $unlikely")
        }
    }

    private fun getNotification(): Notification {
        val intent = Intent(this, LocationUpdatesService::class.java)
        intent.putExtra(extraStartedFromNotification, true)

        // The PendingIntent that leads to a call to onStartCommand() in this service.
        val servicePendingIntent =
            PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val activityPendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java), 0
        )

        val builder = NotificationCompat.Builder(this)
            .addAction(
                R.drawable.ic_launcher_foreground,
                getString(R.string.launch_activity),
                activityPendingIntent
            )
            .addAction(
                R.drawable.ic_cancel,
                getString(R.string.remove_location_updates),
                servicePendingIntent
            )
            // .setContentTitle(getString(R.string.app_name))
            // .setContentText(getString(R.string.reading_location_text))
            .setContentTitle(getTestingLocationTitle())
            .setContentText(getTestingLocationText(lastCoordinates))
            .setOngoing(true)
            .setPriority(Notification.PRIORITY_HIGH)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setWhen(System.currentTimeMillis())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(channelId)
        }

        return builder.build()
    }

    private fun getTestingLocationText(location: Coordinates): String {
        return "Dist: ${lastDistance}m Last: (${location.latitude}/${location.longitude})"
    }

    private fun getTestingLocationTitle(): String {
        return baseContext.getString(
            R.string.location_updated,
            DateFormat.getDateTimeInstance().format(Date())
        )
    }

    inner class LocalBinder : Binder() {
        internal val service: LocationUpdatesService
            get() = this@LocationUpdatesService
    }
}

private const val packageNameString = "com.monday8am.locationupdatesforegroundservice"
private const val extraStartedFromNotification = "$packageNameString.started_from_notification"
private const val maxRadioMeters = 15
