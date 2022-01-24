package com.monday8am.baseapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monday8am.baseapp.domain.Result
import com.monday8am.baseapp.domain.usecase.GetSlowIndex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val index: Int = 0
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getSlowIndex: GetSlowIndex
) : ViewModel(), CoroutineScope {

    private val uiState = MutableStateFlow(MainUiState())
    val mainUiState: StateFlow<MainUiState> = uiState.asStateFlow()

    fun printSlowIndex() {
        launch {
            uiState.update { it.copy(state = DataState.Loading) }
            when (val result = getSlowIndex(Unit)) {
                is Result.Success -> uiState.update {
                    it.copy(index = result.data, state = DataState.Idle)
                }
                is Result.Error -> uiState.update {
                    it.copy(state = DataState.Error("Message!"))
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext
}
