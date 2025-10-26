package com.example.medagenda.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeScreenState(
    val pacienteId: Long? = null,
    val isLoading: Boolean = false
)

sealed interface HomeScreenEvent {
    data object LogoutClicked : HomeScreenEvent
    data class LoadPatientId(val userId: Long) : HomeScreenEvent
}

sealed interface LogoutResult {
    data object Success : LogoutResult
}

class HomeScreenVm(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    private val resultChannel = Channel<LogoutResult>()
    val logoutResults = resultChannel.receiveAsFlow()

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.LogoutClicked -> {
                viewModelScope.launch {
                    resultChannel.send(LogoutResult.Success)
                }
            }
            is HomeScreenEvent.LoadPatientId -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    val paciente = usuarioRepository.findPacienteByUserId(event.userId)
                    _state.update { it.copy(pacienteId = paciente?.idPaciente, isLoading = false) }
                }
            }
        }
    }
}