package com.monday8am.baseapp.domain.usecase

import com.monday8am.baseapp.di.DefaultDispatcher
import com.monday8am.baseapp.domain.FlowUseCase
import com.monday8am.baseapp.domain.Result
import com.monday8am.baseapp.domain.repo.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

open class GetFlowIndex @Inject constructor(
    private val repository: Repository,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : FlowUseCase<Unit, Int>(defaultDispatcher) {

    override fun execute(parameters: Unit): Flow<Result<Int>> {
        return repository.getIndexFlow().map { Result.Success(it) }
    }
}
