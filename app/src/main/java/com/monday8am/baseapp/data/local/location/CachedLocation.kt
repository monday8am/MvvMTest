package com.monday8am.baseapp.data.local.location

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class CachedLocation @JvmOverloads constructor(
    @PrimaryKey val locationId: String,
    val longitude: Double,
    val latitude: Double,
    val imageUrl: String? = null,
    val takenAt: Long
)
