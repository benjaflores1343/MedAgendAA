package com.example.medagenda.ui.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.local.entity.Cita
import com.example.medagenda.data.local.entity.Horario
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface BookingResult {
    object Success : BookingResult
    data class Error(val message: String) : BookingResult
}

class SelectTimeSlotVm(
    private val usuarioRepository: UsuarioRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val medicoId = savedStateHandle.getStateFlow("medicoId", -1L)

    val horariosState: StateFlow<List<Horario>> = medicoId.flatMapLatest { id ->
        if (id != -1L) {
            usuarioRepository.getAvailableHorariosForMedico(id)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = emptyList()
    )

    private val _bookingResult = Channel<BookingResult>()
    val bookingResult = _bookingResult.receiveAsFlow()

    fun bookAppointment(pacienteId: Long, horario: Horario) {
        viewModelScope.launch {
            if (pacienteId == -1L || medicoId.value == -1L) {
                _bookingResult.send(BookingResult.Error("Información de paciente o médico inválida."))
                return@launch
            }
            val newCita = Cita(
                idPaciente = pacienteId,
                idMedico = medicoId.value,
                idHorario = horario.idHorario
            )
            try {
                usuarioRepository.createAppointment(newCita)
                _bookingResult.send(BookingResult.Success)
            } catch (e: Exception) {
                _bookingResult.send(BookingResult.Error(e.message ?: "Ocurrió un error al reservar la cita."))
            }
        }
    }
}