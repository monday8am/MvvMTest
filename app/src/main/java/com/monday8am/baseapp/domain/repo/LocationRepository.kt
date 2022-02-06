package com.monday8am.baseapp.domain.repo

import com.monday8am.baseapp.domain.model.Coordinates
import com.monday8am.baseapp.domain.model.UserLocation
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun addLocation(coordinates: Coordinates)
    fun getLocations(): Flow<List<UserLocation>>
    fun refreshLocationPhotos()
}
