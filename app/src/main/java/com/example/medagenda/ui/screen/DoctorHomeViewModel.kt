package com.example.medagenda.ui.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.network.DoctorAppointmentApiResponse
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DoctorHomeState(
    val isLoading: Boolean = false,
    val appointments: List<DoctorAppointmentApiResponse> = emptyList(),
    val error: String? = null
)

sealed interface DoctorHomeEvent {
    data class ApproveAppointment(val citaId: Long) : DoctorHomeEvent
    data class RejectAppointment(val citaId: Long) : DoctorHomeEvent
}

class DoctorHomeViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DoctorHomeState())
    val state: StateFlow<DoctorHomeState> = _state.asStateFlow()

    private val medicoId: Long? = savedStateHandle.get("medicoId")

    init {
        loadAppointments()
    }

    private fun loadAppointments() {
        medicoId?.let {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                try {
                    val appointments = usuarioRepository.getAppointmentsForDoctor(it)
                    _state.update { s -> s.copy(isLoading = false, appointments = appointments) }
                } catch (e: Exception) {
                    _state.update { s -> s.copy(isLoading = false, error = e.message) }
                }
            }
        }
    }

    fun onEvent(event: DoctorHomeEvent) {
        when (event) {
            is DoctorHomeEvent.ApproveAppointment -> {
                updateAppointmentStatus(event.citaId, "Aprobada")
            }
            is DoctorHomeEvent.RejectAppointment -> {
                updateAppointmentStatus(event.citaId, "Rechazada")
            }
        }
    }

    private fun updateAppointmentStatus(citaId: Long, status: String) {
        viewModelScope.launch {
            try {
                usuarioRepository.updateAppointmentStatus(citaId, status)
                // Refresh the list of appointments after updating the status
                loadAppointments()
            } catch (e: Exception) {
                _state.update { it.copy(error = "Error al actualizar el estado de la cita.") }
            }
        }
    }
}