package com.monday8am.baseapp.data.local.location

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Query("SELECT * FROM Photos")
    fun getLocationsFlow(): Flow<List<CachedLocation>>

    @Query("SELECT * FROM Photos WHERE imageUrl IS NULL")
    suspend fun getLocationsWithoutPhotos(): List<CachedLocation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cachedLocation: CachedLocation)
}
