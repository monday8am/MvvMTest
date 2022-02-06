package com.monday8am.baseapp.data

import com.monday8am.baseapp.data.local.AppDatabase
import com.monday8am.baseapp.data.local.user.CachedUser
import com.monday8am.baseapp.data.remote.UserClient
import com.monday8am.baseapp.di.DefaultDispatcher
import com.monday8am.baseapp.domain.model.User
import com.monday8am.baseapp.domain.repo.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserClient,
    private val database: AppDatabase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository, CoroutineScope {

    private val internalJob = Job()

    override fun getUsers(): Flow<List<User>> {
        launch {
            try {
                val isEmpty = database.userDao().getUsers().isEmpty()
                if (isEmpty) {
                    val users = userDataSource.getUsers()
                    database.userDao().insert(users.map { it.toCached() })
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }

        return database.userDao().getUsersFlow().map { cachedList -> cachedList.map { it.toUser() } }
    }

    override suspend fun addUser(user: User) {
        database.userDao().insert(user.toCached())
    }

    override suspend fun removeUser(userId: String) {
        database.userDao().delete(userId)
    }

    private fun CachedUser.toUser(): User {
        return User(
            name = name,
            position = position,
            platform = platform,
            pic = pic
        )
    }

    private fun User.toCached(): CachedUser {
        return CachedUser(
            name = name,
            position = position,
            platform = platform,
            pic = pic,
            updatedAt = System.currentTimeMillis()
        )
    }

    override val coroutineContext: CoroutineContext
        get() = defaultDispatcher + internalJob
}
