package com.monday8am.baseapp.data

import com.monday8am.baseapp.domain.repo.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class RepositoryImpl @Inject constructor() : Repository {

    override fun getIndex(): Int {
        return 1
    }

    override fun getIndexFlow(): Flow<Int> {
        return flowOf(2, 3, 4)
    }

    override suspend fun getSlowIndex(): Int {
        delay(4000L)
        return 5
    }
}
