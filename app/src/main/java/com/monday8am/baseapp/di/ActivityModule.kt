package com.monday8am.baseapp.di

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.monday8am.baseapp.ui.ScreenNavigator
import com.monday8am.baseapp.ui.ScreenNavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @Provides
    fun providesScreenNavigator(activity: Activity): ScreenNavigator {
        check(activity is AppCompatActivity)
        return ScreenNavigatorImpl(activity)
    }
}
