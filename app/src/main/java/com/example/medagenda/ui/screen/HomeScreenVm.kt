package com.example.medagenda.ui.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.local.dto.DoctorAppointmentInfo
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeScreenState(
    val pacienteId: Long? = null,
    val medicoId: Long? = null,
    val doctorAppointments: List<DoctorAppointmentInfo> = emptyList(),
    val isLoading: Boolean = false
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
                        _state.update { it.copy(isLoading = true) }
                        val paciente = usuarioRepository.findPacienteByUserId(userId)
                        _state.update { it.copy(pacienteId = paciente?.idPaciente, isLoading = false) }
                    }
                }
                "Médico" -> {
                    viewModelScope.launch {
                        _state.update { it.copy(isLoading = true) }
                        val medico = usuarioRepository.findMedicoByUserId(userId)
                        if (medico != null) {
                            _state.update { it.copy(medicoId = medico.idMedico) }
                            usuarioRepository.getAppointmentsForDoctor(medico.idMedico)
                                .onEach { appointments ->
                                    _state.update { 
                                        it.copy(doctorAppointments = appointments, isLoading = false) 
                                    }
                                }
                                .launchIn(viewModelScope)
                        } else {
                            _state.update { it.copy(isLoading = false) }
                        }
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