package com.monday8am.baseapp.data

import com.monday8am.baseapp.data.local.location.CachedLocation
import com.monday8am.baseapp.data.local.location.LocationDao
import com.monday8am.baseapp.data.remote.FlickrClient
import com.monday8am.baseapp.di.DefaultDispatcher
import com.monday8am.baseapp.domain.model.Coordinates
import com.monday8am.baseapp.domain.model.UserLocation
import com.monday8am.baseapp.domain.repo.LocationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LocationRepositoryImpl @Inject constructor(
    private val remoteClient: FlickrClient,
    private val locationDao: LocationDao,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocationRepository, CoroutineScope {

    override fun getLocations(): Flow<List<UserLocation>> {
        return locationDao.getLocationsFlow().map { it.map { cached -> cached.toUserLocation() } }
    }

    override fun refreshLocationPhotos() {
        launch {
            val items = locationDao.getLocationsWithoutPhotos()
            items.forEach {
                updateLocationWithPhoto(it)
            }
        }
    }

    override fun addLocation(coordinates: Coordinates) {
        launch {
            val newItem = UserLocation(longitude = coordinates.longitude, latitude = coordinates.latitude)
            with(newItem.toCached()) {
                locationDao.insert(this)
                updateLocationWithPhoto(this)
            }
        }
    }

    private suspend fun updateLocationWithPhoto(userLocation: CachedLocation) {
        try {
            val result = remoteClient.listPhotos(
                "2e543b92f08024a1b91b274f75727416",
                longitude = userLocation.longitude,
                latitude = userLocation.latitude
            )
            val updated = userLocation.copy(imageUrl = result.photos?.photo?.first()?.url_c)
            locationDao.insert(updated)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun CachedLocation.toUserLocation(): UserLocation {
        return UserLocation(
            id = locationId,
            longitude = longitude,
            latitude = latitude,
            imageUrl = imageUrl,
            takenAt = takenAt
        )
    }

    private fun UserLocation.toCached(): CachedLocation {
        return CachedLocation(
            locationId = id,
            longitude = longitude,
            latitude = latitude,
            imageUrl = imageUrl,
            takenAt = takenAt
        )
    }

    override val coroutineContext: CoroutineContext
        get() = Job() + defaultDispatcher
}
