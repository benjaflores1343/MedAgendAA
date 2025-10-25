package com.example.medagenda.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed interface HomeScreenEvent {
    data object LogoutClicked : HomeScreenEvent
}

sealed interface LogoutResult {
    data object Success : LogoutResult
}

class HomeScreenVm : ViewModel() {

    private val resultChannel = Channel<LogoutResult>()
    val logoutResults = resultChannel.receiveAsFlow()

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.LogoutClicked -> {
                viewModelScope.launch {
                    resultChannel.send(LogoutResult.Success)
                }
            }
        }
    }
}