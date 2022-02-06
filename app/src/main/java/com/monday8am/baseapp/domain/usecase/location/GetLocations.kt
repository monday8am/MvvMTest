package com.monday8am.baseapp.domain.usecase.location

import com.monday8am.baseapp.di.DefaultDispatcher
import com.monday8am.baseapp.domain.FlowUseCase
import com.monday8am.baseapp.domain.Result
import com.monday8am.baseapp.domain.model.UserLocation
import com.monday8am.baseapp.domain.repo.LocationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

open class GetLocations @Inject constructor(
    private val locationRepository: LocationRepository,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : FlowUseCase<Unit, List<UserLocation>>(defaultDispatcher) {

    override fun execute(parameters: Unit): Flow<Result<List<UserLocation>>> {
        return locationRepository.getLocations()
            .map { Result.Success(it.sortedByDescending { location -> location.takenAt }) }
    }
}
