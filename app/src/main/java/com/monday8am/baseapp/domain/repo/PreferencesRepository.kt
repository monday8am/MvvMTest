package com.monday8am.baseapp.domain.repo

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val isLocationRequestedFlow: Flow<Boolean>
    var isLocationRequested: Boolean
}
