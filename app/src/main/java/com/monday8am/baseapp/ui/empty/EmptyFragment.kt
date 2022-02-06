package com.monday8am.baseapp.ui.empty

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.button.MaterialButton
import com.monday8am.baseapp.R
import com.monday8am.baseapp.ui.launchAndRepeatWithViewLifecycle
import com.monday8am.baseapp.ui.main.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class EmptyFragment : Fragment(R.layout.empty_fragment) {

    private val viewModel: EmptyViewModel by viewModels()

    private lateinit var btnAction: MaterialButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAction = view.findViewById(R.id.btn_action)
        setHasOptionsMenu(true)
        setupToolbar()
        setupBtns()

        launchAndRepeatWithViewLifecycle {
            viewModel.emptyUiState.collect {
                when (it.state) {
                    DataState.Idle -> Timber.d("Update UI! ${it.index}")
                    DataState.Loading -> Timber.d("Loading")
                    is DataState.Error -> Timber.d("Error! ${it.state.message}")
                }
            }
        }
    }

    private fun setupToolbar() {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupBtns() {
        btnAction.setOnClickListener {
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
