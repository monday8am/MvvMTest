package com.monday8am.baseapp.domain.usecase

import com.monday8am.baseapp.di.DefaultDispatcher
import com.monday8am.baseapp.domain.SuspendUseCase
import com.monday8am.baseapp.domain.repo.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

open class GetSlowIndex @Inject constructor(
    private val repository: Repository,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SuspendUseCase<Unit, Int>(defaultDispatcher) {

    override suspend fun execute(parameters: Unit): Int {
        return repository.getSlowIndex()
    }
}
