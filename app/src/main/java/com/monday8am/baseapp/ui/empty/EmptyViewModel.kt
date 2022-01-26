package com.monday8am.baseapp.ui.empty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monday8am.baseapp.domain.usecase.GetUsers
import com.monday8am.baseapp.ui.main.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

data class EmptyUiState(
    val state: DataState = DataState.Idle,
    val index: Int = 0
)

@HiltViewModel
class EmptyViewModel @Inject constructor(
    private val getFlowIndex: GetUsers
) : ViewModel(), CoroutineScope {

    private val uiState = MutableStateFlow(EmptyUiState())
    val emptyUiState: StateFlow<EmptyUiState> = uiState.asStateFlow()

    init {
        uiState.update { it.copy(state = DataState.Loading) }
    }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext
}
