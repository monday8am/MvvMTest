package com.monday8am.baseapp.ui.empty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monday8am.baseapp.domain.Result
import com.monday8am.baseapp.domain.usecase.GetFlowIndex
import com.monday8am.baseapp.ui.main.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

data class EmptyUiState(
    val state: DataState = DataState.Idle,
    val index: Int = 0
)

@HiltViewModel
class EmptyViewModel @Inject constructor(
    private val getFlowIndex: GetFlowIndex
) : ViewModel(), CoroutineScope {

    private val uiState = MutableStateFlow(EmptyUiState())
    val emptyUiState: StateFlow<EmptyUiState> = uiState.asStateFlow()

    init {
        uiState.update { it.copy(state = DataState.Loading) }

        launch {
            getFlowIndex(Unit).collect { result ->
                when (result) {
                    is Result.Success -> uiState.update {
                        it.copy(index = result.data, state = DataState.Idle)
                    }
                    is Result.Error -> uiState.update {
                        it.copy(state = DataState.Error("Message!"))
                    }
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext
}
