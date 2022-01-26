package com.monday8am.baseapp.data.local.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: CachedUser)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(users: List<CachedUser>)

    @Query("SELECT * FROM user_table")
    fun getUsersFlow(): Flow<List<CachedUser>>

    @Query("SELECT * FROM user_table")
    suspend fun getUsers(): List<CachedUser>

    @Query("DELETE FROM user_table WHERE name = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()
}
