package com.example.medagenda.ui.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.local.dto.AppointmentFullDetails
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AppointmentDetailState(
    val appointment: AppointmentFullDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class AppointmentDetailVm(
    private val usuarioRepository: UsuarioRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(AppointmentDetailState())
    val state = _state.asStateFlow()

    init {
        savedStateHandle.get<Long>("citaId")?.let {
            getAppointmentDetails(it)
        }
    }

    private fun getAppointmentDetails(citaId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val details = usuarioRepository.getAppointmentDetails(citaId)
                _state.update {
                    it.copy(
                        isLoading = false,
                        appointment = details
                    )
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(isLoading = false, error = "Error al cargar los detalles de la cita") 
                }
            }
        }
    }
}