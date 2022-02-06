package com.monday8am.baseapp.di

import android.app.Application
import android.content.Context
import com.monday8am.baseapp.data.LocationRepositoryImpl
import com.monday8am.baseapp.data.PreferencesRepositoryImpl
import com.monday8am.baseapp.data.UserRepositoryImpl
import com.monday8am.baseapp.data.local.AppDatabase
import com.monday8am.baseapp.data.remote.FlickrClient
import com.monday8am.baseapp.data.remote.RetrofitClientFactory
import com.monday8am.baseapp.data.remote.UserClient
import com.monday8am.baseapp.data.remote.flickrUrl
import com.monday8am.baseapp.domain.repo.LocationRepository
import com.monday8am.baseapp.domain.repo.PreferencesRepository
import com.monday8am.baseapp.domain.repo.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

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
    fun providesUserDatabase(application: Application): AppDatabase =
        AppDatabase.getInstance(application)

    @Singleton
    @Provides
    fun providesUserClient(serializer: Json): UserClient {
        return RetrofitClientFactory.create(serializer, "UserBaseUrl", UserClient::class.java)
    }

    @Singleton
    @Provides
    fun providesPhotoClient(serializer: Json): FlickrClient {
        return RetrofitClientFactory.create(serializer, flickrUrl, FlickrClient::class.java)
    }

    @Provides
    @Singleton
    fun providesUserRepository(database: AppDatabase, userClient: UserClient): UserRepository {
        return UserRepositoryImpl(userClient, database)
    }

    @Provides
    @Singleton
    fun providesLocationRepository(flickrClient: FlickrClient, database: AppDatabase): LocationRepository {
        return LocationRepositoryImpl(flickrClient, database.locationDao())
    }

    @Provides
    @Singleton
    fun providesPreferencesRepository(@ApplicationContext appContext: Context): PreferencesRepository =
        PreferencesRepositoryImpl(appContext)
}
