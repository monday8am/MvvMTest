package com.monday8am.baseapp.di

import android.app.Application
import com.monday8am.baseapp.data.RepositoryImpl
import com.monday8am.baseapp.data.local.UserDatabase
import com.monday8am.baseapp.data.remote.RemoteClient
import com.monday8am.baseapp.data.remote.RetrofitClientFactory
import com.monday8am.baseapp.domain.repo.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServicesModule {

    @Singleton
    @Provides
    fun providesSerializer(): Json {
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
        }
    }

    @Singleton
    @Provides
    fun providesUserDatabase(application: Application): UserDatabase =
        UserDatabase.getInstance(application)

    @Singleton
    @Provides
    fun providesRemoteClientClient(serializer: Json): RemoteClient {
        return RetrofitClientFactory(serializer).create()
    }

    @Provides
    fun providesRepository(database: UserDatabase, remoteClient: RemoteClient): Repository {
        return RepositoryImpl(remoteClient, database)
    }
}
