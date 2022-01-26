package com.monday8am.baseapp.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.button.MaterialButton
import com.monday8am.baseapp.R
import com.monday8am.baseapp.ui.ScreenNavigator
import com.monday8am.baseapp.ui.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {

    @Inject
    lateinit var screenNavigator: ScreenNavigator

    private val viewModel: MainViewModel by viewModels()

    private lateinit var btnAction: MaterialButton
    private lateinit var btnNavigate: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAction = view.findViewById(R.id.btn_action)
        btnNavigate = view.findViewById(R.id.btn_navigate)
        setupUI()
        setupToolbar()

        launchAndRepeatWithViewLifecycle {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mainUiState.collect {
                    when (it.state) {
                        DataState.Idle -> Timber.d("Update UI! ${it.users}")
                        DataState.Loading -> Timber.d("Loading")
                        is DataState.Error -> Timber.d("Error! ${it.state.message}")
                    }
                }
            }
        }
    }

    private fun setupUI() {
        btnAction.setOnClickListener {
            viewModel.printSlowIndex()
        }

        btnNavigate.setOnClickListener {
            screenNavigator.goToEmpty()
        }
    }

    private fun setupToolbar() {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
        }
    }
}
