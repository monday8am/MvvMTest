package com.monday8am.baseapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monday8am.baseapp.domain.Result
import com.monday8am.baseapp.domain.data
import com.monday8am.baseapp.domain.model.UserLocation
import com.monday8am.baseapp.domain.usecase.location.GetLocations
import com.monday8am.baseapp.domain.usecase.location.IsLocationRequested
import com.monday8am.baseapp.domain.usecase.location.RefreshLocations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

sealed class DataState {
    object Idle : DataState()
    object Loading : DataState()
    data class Error(val message: String) : DataState()
}

data class MainUiState(
    val state: DataState = DataState.Idle,
    val isLocationRequested: Boolean = false,
    val locations: List<UserLocation> = listOf()
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getLocations: GetLocations,
    private val isLocationRequested: IsLocationRequested,
    refreshLocations: RefreshLocations
) : ViewModel(), CoroutineScope {

    private val uiState = MutableStateFlow(MainUiState())
    val mainUiState: StateFlow<MainUiState> = uiState.asStateFlow()

    init {
        uiState.update { it.copy(state = DataState.Loading) }
        refreshLocations(Unit)

        launch {
            getLocations(Unit).collect { result ->
                when (result) {
                    is Result.Success -> uiState.update {
                        it.copy(locations = result.data, state = DataState.Idle)
                    }
                    is Result.Error -> uiState.update {
                        it.copy(state = DataState.Error("Message!"))
                    }
                }
            }
        }

        launch {
            isLocationRequested(Unit).collect { result ->
                uiState.update {
                    it.copy(isLocationRequested = result.data ?: false)
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext
}
