package com.monday8am.baseapp.domain.model

import android.location.Location
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

typealias Meters = Int

@Serializable
data class Coordinates(val longitude: Double, val latitude: Double) {

    fun isOutsideOf(lastCoordinates: Coordinates, radio: Meters = 100): Boolean {
        return distanceTo(lastCoordinates) > radio
    }

    fun distanceTo(lastCoordinates: Coordinates): Meters {
        val locationA = Location("A")
        locationA.longitude = longitude
        locationA.latitude = latitude

        val locationB = Location("B")
        locationB.longitude = lastCoordinates.longitude
        locationB.latitude = lastCoordinates.latitude

        return locationA.distanceTo(locationB).roundToInt()
    }
}
