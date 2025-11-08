package com.example.medagenda.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeScreenState(
    val isLoading: Boolean = false,
    val pacienteId: Long? = null,
    val error: String? = null
)

sealed interface HomeScreenEvent {
    data class LoadPatientId(val userId: Long) : HomeScreenEvent
    object LogoutClicked : HomeScreenEvent
}

sealed interface LogoutResult {
    object Success : LogoutResult
}

class HomeScreenVm(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    private val logoutChannel = Channel<LogoutResult>()
    val logoutResults = logoutChannel.receiveAsFlow()

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.LoadPatientId -> {
                loadPatientId(event.userId)
            }
            is HomeScreenEvent.LogoutClicked -> {
                viewModelScope.launch {
                    logoutChannel.send(LogoutResult.Success)
                }
            }
        }
    }

    private fun loadPatientId(userId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val paciente = usuarioRepository.findPacienteByUserId(userId)
            if (paciente != null) {
                _state.update { it.copy(isLoading = false, pacienteId = paciente.idPaciente) }
            } else {
                _state.update { it.copy(isLoading = false, error = "No se pudo encontrar el perfil del paciente.") }
            }
        }
    }
}