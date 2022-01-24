package com.monday8am.baseapp.di

import com.monday8am.baseapp.data.RepositoryImpl
import com.monday8am.baseapp.domain.repo.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ServicesModule {

    @Provides
    fun providesRepository(): Repository = RepositoryImpl()

    /*
    @Singleton
    @Provides
    fun providesRepository(application: Application): Repository = RepositoryImpl()
     */
}
