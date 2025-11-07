package com.example.medagenda.ui.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.local.dto.DoctorAppointmentInfo
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.*

data class DoctorHomeState(
    val isLoading: Boolean = false,
    val appointments: List<DoctorAppointmentInfo> = emptyList(),
    val error: String? = null
)

class DoctorHomeViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val medicoId = savedStateHandle.getStateFlow("medicoId", -1L)

    val state: StateFlow<DoctorHomeState> = medicoId.flatMapLatest { id ->
        if (id != -1L) {
            usuarioRepository.getAppointmentsForDoctor(id)
                .map { appointments -> DoctorHomeState(isLoading = false, appointments = appointments) }
                .onStart { emit(DoctorHomeState(isLoading = true)) }
                .catch { e -> emit(DoctorHomeState(isLoading = false, error = e.message)) }
        } else {
            flowOf(DoctorHomeState(isLoading = false, error = "No se pudo obtener el ID del médico"))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = DoctorHomeState(isLoading = true)
    )
}