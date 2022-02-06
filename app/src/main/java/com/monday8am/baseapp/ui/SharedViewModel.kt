package com.monday8am.baseapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ServiceAction {
    START,
    STOP,
    NOTHING
}

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    private val uiEvents = MutableSharedFlow<ServiceAction>()
    val sharedUiEvents: SharedFlow<ServiceAction> = uiEvents

    fun startRequestLocation() = viewModelScope.launch {
        uiEvents.emit(ServiceAction.START)
    }

    fun stopRequestLocation() = viewModelScope.launch {
        uiEvents.emit(ServiceAction.STOP)
    }
}
