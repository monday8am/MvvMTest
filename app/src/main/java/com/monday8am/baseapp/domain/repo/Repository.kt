package com.monday8am.baseapp.domain.repo

import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getIndex(): Int
    fun getIndexFlow(): Flow<Int>
    suspend fun getSlowIndex(): Int
}
