package com.example.medagenda.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.local.entity.Especialidad
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.*

data class RequestAppointmentState(
    val especialidades: List<Especialidad> = emptyList(),
    val isLoading: Boolean = false
)

class RequestAppointmentVm(
    usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RequestAppointmentState())
    val state: StateFlow<RequestAppointmentState> = _state.asStateFlow()

    init {
        usuarioRepository.getAllEspecialidades()
            .onStart { _state.update { it.copy(isLoading = true) } }
            .onEach { especialidades ->
                _state.update {
                    it.copy(
                        especialidades = especialidades,
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}