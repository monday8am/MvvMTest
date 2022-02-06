package com.monday8am.baseapp.domain.usecase.location

import com.monday8am.baseapp.di.DefaultDispatcher
import com.monday8am.baseapp.domain.FlowUseCase
import com.monday8am.baseapp.domain.Result
import com.monday8am.baseapp.domain.repo.PreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

open class IsLocationRequested @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : FlowUseCase<Unit, Boolean>(defaultDispatcher) {

    override fun execute(parameters: Unit): Flow<Result<Boolean>> {
        return preferencesRepository.isLocationRequestedFlow.map { Result.Success(it) }
    }
}
