package com.monday8am.baseapp.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.monday8am.baseapp.R
import com.monday8am.baseapp.ui.empty.EmptyFragment
import com.monday8am.baseapp.ui.main.MainFragment

interface ScreenNavigator {
    fun goToMain()
    fun goToEmpty()
}

class ScreenNavigatorImpl(
    private val activity: AppCompatActivity
) : ScreenNavigator {

    override fun goToMain() {
        replaceFragment(MainFragment(), addToBackStack = false)
    }

    override fun goToEmpty() {
        replaceFragment(EmptyFragment())
    }

    private fun addFragment(fragment: Fragment) {
        val tag = fragment::class.simpleName ?: ""
        if (activity.supportFragmentManager.findFragmentByTag(tag) != null) return
        activity.supportFragmentManager.commit {
            add(R.id.container_main, fragment, tag)
            addToBackStack(tag)
        }
    }

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val tag = fragment::class.simpleName ?: ""
        activity.supportFragmentManager.commit {
            replace(R.id.container_main, fragment, tag)
            if (addToBackStack) {
                addToBackStack(tag)
            }
        }
    }
}
