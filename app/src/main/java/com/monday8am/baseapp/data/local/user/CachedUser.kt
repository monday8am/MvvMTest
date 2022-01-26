package com.monday8am.baseapp.data.local.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class CachedUser(
    @PrimaryKey val id: String,
    val name: String,
    val position: String,
    val platform: String,
    val pic: String,
    val updatedAt: Long = System.currentTimeMillis()
)
