package com.monday8am.baseapp.domain.usecase

import com.monday8am.baseapp.di.DefaultDispatcher
import com.monday8am.baseapp.domain.SuspendUseCase
import com.monday8am.baseapp.domain.model.User
import com.monday8am.baseapp.domain.repo.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

open class AddUser @Inject constructor(
    private val repository: Repository,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SuspendUseCase<User, Unit>(defaultDispatcher) {

    override suspend fun execute(parameters: User) {
        return repository.addUser(parameters)
    }
}
