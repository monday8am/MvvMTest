package com.monday8am.baseapp.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.monday8am.baseapp.R
import com.monday8am.baseapp.ui.ScreenNavigator
import com.monday8am.baseapp.ui.SharedViewModel
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
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var startMenuItem: MenuItem? = null
    private var stopMenuItem: MenuItem? = null
    private lateinit var locationRecycler: RecyclerView
    private val locationListAdapter = LocationListTestingAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationRecycler = view.findViewById(R.id.recycler_locations_main)
        setupToolbar()
        setupUI()

        launchAndRepeatWithViewLifecycle {
            viewModel.mainUiState.collect {
                when (it.state) {
                    DataState.Idle -> locationListAdapter.submitList(it.locations)
                    DataState.Loading -> Timber.d("Loading")
                    is DataState.Error -> Timber.d("Error! ${it.state.message}")
                }
                startMenuItem?.isVisible = !it.isLocationRequested
                stopMenuItem?.isVisible = it.isLocationRequested
            }
        }
    }

    private fun setupToolbar() {
        setHasOptionsMenu(true)
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
        }
    }

    private fun setupUI() {
        locationRecycler.apply {
            adapter = locationListAdapter
            setHasFixedSize(true)
            (itemAnimator as DefaultItemAnimator).run {
                supportsChangeAnimations = false
                addDuration = 160L
                moveDuration = 160L
                changeDuration = 160L
                removeDuration = 120L
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_start_stop, menu)
        startMenuItem = menu.findItem(R.id.action_start)
        stopMenuItem = menu.findItem(R.id.action_stop)

        val isActivated = viewModel.mainUiState.value.isLocationRequested
        startMenuItem?.isVisible = !isActivated
        stopMenuItem?.isVisible = isActivated
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_start -> {
                sharedViewModel.startRequestLocation()
                return true
            }
            R.id.action_stop -> {
                sharedViewModel.stopRequestLocation()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
