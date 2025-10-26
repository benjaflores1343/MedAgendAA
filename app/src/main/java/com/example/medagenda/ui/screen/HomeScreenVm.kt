package com.example.medagenda.ui.screen

import androidx.lifecycle.SavedStateHandle
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
    val pacienteId: Long? = null,
    val medicoId: Long? = null
)

sealed interface HomeScreenEvent {
    data object LogoutClicked : HomeScreenEvent
}

sealed interface LogoutResult {
    data object Success : LogoutResult
}

class HomeScreenVm(
    private val usuarioRepository: UsuarioRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    private val resultChannel = Channel<LogoutResult>()
    val logoutResults = resultChannel.receiveAsFlow()

    init {
        val userId = savedStateHandle.get<Long>("userId")
        val userRole = savedStateHandle.get<String>("userRole")

        if (userId != null && userRole != null) {
            when (userRole) {
                "Paciente" -> {
                    viewModelScope.launch {
                        val paciente = usuarioRepository.findPacienteByUserId(userId)
                        _state.update { it.copy(pacienteId = paciente?.idPaciente) }
                    }
                }
                "Médico" -> {
                    viewModelScope.launch {
                        val medico = usuarioRepository.findMedicoByUserId(userId)
                        _state.update { it.copy(medicoId = medico?.idMedico) }
                    }
                }
            }
        }
    }

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