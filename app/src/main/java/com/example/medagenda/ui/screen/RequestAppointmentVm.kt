package com.example.medagenda.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.network.EspecialidadApi
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class RequestAppointmentState(
    val especialidades: List<EspecialidadApi> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class RequestAppointmentVm(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RequestAppointmentState())
    val state: StateFlow<RequestAppointmentState> = _state.asStateFlow()

    init {
        loadEspecialidades()
    }

    private fun loadEspecialidades() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val especialidades = usuarioRepository.getAllEspecialidades()
                _state.update { it.copy(isLoading = false, especialidades = especialidades) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}