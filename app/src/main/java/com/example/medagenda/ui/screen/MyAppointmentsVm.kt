package com.example.medagenda.ui.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.network.AppointmentApiResponse
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MyAppointmentsState(
    val isLoading: Boolean = false,
    val appointments: List<AppointmentApiResponse> = emptyList(),
    val error: String? = null
)

class MyAppointmentsVm(
    private val usuarioRepository: UsuarioRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(MyAppointmentsState())
    val state: StateFlow<MyAppointmentsState> = _state.asStateFlow()

    init {
        val patientId: Long? = savedStateHandle.get("patientId")
        if (patientId != null) {
            loadAppointments(patientId)
        } else {
            _state.update { it.copy(isLoading = false, error = "No se pudo obtener el ID del paciente.") }
        }
    }

    private fun loadAppointments(patientId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val appointments = usuarioRepository.getAppointmentsForPatient(patientId)
                _state.update { it.copy(isLoading = false, appointments = appointments) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}