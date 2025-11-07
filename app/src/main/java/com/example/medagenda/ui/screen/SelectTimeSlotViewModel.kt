package com.example.medagenda.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.local.entity.Cita
import com.example.medagenda.data.local.entity.Horario
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class SelectTimeSlotState(
    val isLoading: Boolean = true,
    val timeSlots: List<Horario> = emptyList(),
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
            _state.value = SelectTimeSlotState(isLoading = true)
            repository.getAvailableHorariosForMedico(medicoId)
                .catch { e ->
                    _state.value = SelectTimeSlotState(isLoading = false, error = e.message)
                }
                .collect { timeSlots ->
                    _state.value = SelectTimeSlotState(isLoading = false, timeSlots = timeSlots)
                }
        }
    }

    fun bookAppointment(pacienteId: Long, timeSlot: Horario, medicoId: Long) {
        viewModelScope.launch {
            // First, check if the time slot is still available
            val isTimeSlotAvailable = repository.isTimeSlotAvailable(timeSlot.idHorario)

            if (isTimeSlotAvailable) {
                try {
                    val newCita = Cita(
                        idPaciente = pacienteId,
                        idMedico = medicoId,
                        idHorario = timeSlot.idHorario,
                        estado = "Programada"
                    )
                    repository.createAppointment(newCita)
                    _state.value = state.value.copy(bookingResult = Result.success(Unit))
                } catch (e: Exception) {
                    _state.value = state.value.copy(bookingResult = Result.failure(e))
                }
            } else {
                // Time slot is already booked
                _state.value = state.value.copy(
                    bookingResult = Result.failure(Exception("Este horario ya ha sido reservado. Por favor, seleccione otro.")),
                    error = "Este horario ya ha sido reservado. Por favor, seleccione otro."
                )
            }
        }
    }
}