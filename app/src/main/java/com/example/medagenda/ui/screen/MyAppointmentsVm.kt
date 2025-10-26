package com.example.medagenda.ui.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.local.dto.AppointmentInfo
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.*

class MyAppointmentsVm(
    usuarioRepository: UsuarioRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val patientId = savedStateHandle.getStateFlow("patientId", -1L)

    val appointmentsState: StateFlow<List<AppointmentInfo>> = patientId.flatMapLatest { id ->
        if (id != -1L) {
            usuarioRepository.getAppointmentsForPatient(id)
                .onStart { _isLoading.value = true }
                .onCompletion { _isLoading.value = false }
        } else {
            flowOf(emptyList())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = emptyList()
    )
}