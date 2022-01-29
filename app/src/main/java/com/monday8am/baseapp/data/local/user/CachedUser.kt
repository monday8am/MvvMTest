package com.monday8am.baseapp.data.local.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class CachedUser(
    @PrimaryKey val name: String,
    val position: String,
    val platform: String,
    val pic: String? = null,
    val updatedAt: Long = System.currentTimeMillis()
)
