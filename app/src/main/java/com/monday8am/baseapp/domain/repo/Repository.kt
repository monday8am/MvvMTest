package com.monday8am.baseapp.domain.repo

import com.monday8am.baseapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getUsers(): Flow<List<User>>
    suspend fun addUser(user: User)
    suspend fun removeUser(userId: String)
    suspend fun sort()
}
