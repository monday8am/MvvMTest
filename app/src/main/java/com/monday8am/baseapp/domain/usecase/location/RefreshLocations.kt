package com.monday8am.baseapp.domain.usecase.location

import com.monday8am.baseapp.di.DefaultDispatcher
import com.monday8am.baseapp.domain.UseCase
import com.monday8am.baseapp.domain.repo.LocationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

open class RefreshLocations @Inject constructor(
    private val repository: LocationRepository,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<Unit, Unit>(defaultDispatcher) {

    override fun execute(parameters: Unit) {
        return repository.refreshLocationPhotos()
    }
}
