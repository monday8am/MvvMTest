package com.monday8am.baseapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.monday8am.baseapp.ui.ScreenNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val INITIATED_KEY = "initiated"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var screenNavigator: ScreenNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState?.getBoolean(INITIATED_KEY) == null) {
            screenNavigator.goToMain()
            savedInstanceState?.putBoolean(INITIATED_KEY, true)
        }
    }
}
