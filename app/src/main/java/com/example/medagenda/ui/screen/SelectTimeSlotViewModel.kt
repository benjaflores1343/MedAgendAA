package com.example.medagenda.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.network.CreateAppointmentRequest
import com.example.medagenda.data.network.HorarioApi
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SelectTimeSlotState(
    val isLoading: Boolean = true,
    val timeSlots: List<HorarioApi> = emptyList(),
    val error: String? = null,
    val bookingResult: Result<Unit>? = null
)

class SelectTimeSlotViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SelectTimeSlotState())
    val state: StateFlow<SelectTimeSlotState> = _state.asStateFlow()

    fun loadAvailableTimeSlots(medicoId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val timeSlots = repository.getAvailableHorariosForMedico(medicoId)
                _state.update { it.copy(isLoading = false, timeSlots = timeSlots) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun bookAppointment(pacienteId: Long, timeSlot: HorarioApi, medicoId: Long) {
        viewModelScope.launch {
            try {
                val createAppointmentRequest = CreateAppointmentRequest(
                    idPaciente = pacienteId,
                    idMedico = medicoId,
                    idHorario = timeSlot.idHorario
                )
                repository.createAppointment(createAppointmentRequest)
                _state.value = state.value.copy(bookingResult = Result.success(Unit))
            } catch (e: Exception) {
                _state.value = state.value.copy(bookingResult = Result.failure(e))
            }
        }
    }
}