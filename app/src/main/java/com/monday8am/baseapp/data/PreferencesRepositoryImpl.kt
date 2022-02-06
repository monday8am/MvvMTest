package com.monday8am.baseapp.data

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import com.monday8am.baseapp.domain.repo.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PreferencesRepositoryImpl @Inject constructor(context: Context) :
    PreferencesRepository,
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val prefs: Lazy<SharedPreferences> = lazy {
        // Lazy to prevent IO access to main thread.
        context.applicationContext.getSharedPreferences(
            SETTINGS_PREF_NAME, Context.MODE_PRIVATE
        )
    }

    override var isLocationRequested: Boolean by BooleanPreference(
        prefs,
        KEY_IS_GETTING_LOCATION,
        false
    )

    private var locationFlow: MutableStateFlow<Boolean> = MutableStateFlow(isLocationRequested)
    override val isLocationRequestedFlow: Flow<Boolean>
        get() = locationFlow

    init {
        prefs.value.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            KEY_IS_GETTING_LOCATION -> locationFlow.value = isLocationRequested
            else -> {}
        }
    }

    companion object {
        const val KEY_IS_GETTING_LOCATION = "user_token"
    }
}

class BooleanPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Boolean
) : ReadWriteProperty<Any, Boolean> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return preferences.value.getBoolean(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        preferences.value.edit().putBoolean(name, value).apply()
    }
}

private const val SETTINGS_PREF_NAME = "settings.app"
