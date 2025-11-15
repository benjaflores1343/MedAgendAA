package com.example.medagenda.ui.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.local.dto.AppointmentFullDetails
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// The AppointmentDetailState data class is now defined in AppointmentDetailVm.kt
// and will be shared by both ViewModels.

class DoctorAppointmentDetailViewModel(
    private val usuarioRepository: UsuarioRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(AppointmentDetailState())
    val state = _state.asStateFlow()

    init {
        val citaId: Long? = savedStateHandle.get("citaId")
        if (citaId != null) {
            loadAppointmentDetails(citaId)
        } else {
            _state.value = AppointmentDetailState(isLoading = false, error = "No se pudo encontrar la cita.")
        }
    }

    private fun loadAppointmentDetails(citaId: Long) {
        viewModelScope.launch {
            _state.value = AppointmentDetailState(isLoading = true)
            try {
                val appointmentDetails = usuarioRepository.getAppointmentDetails(citaId)
                _state.value = AppointmentDetailState(isLoading = false, appointment = appointmentDetails)
            } catch (e: Exception) {
                _state.value = AppointmentDetailState(isLoading = false, error = e.message)
            }
        }
    }
}