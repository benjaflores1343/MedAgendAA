package com.example.medagenda.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.network.MedicoApi
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SelectDoctorState(
    val isLoading: Boolean = true,
    val doctors: List<MedicoApi> = emptyList(),
    val error: String? = null
)

class SelectDoctorViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SelectDoctorState())
    val state: StateFlow<SelectDoctorState> = _state.asStateFlow()

    fun loadDoctorsBySpecialty(specialtyId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val doctors = repository.getMedicosByEspecialidad(specialtyId)
                _state.update { it.copy(isLoading = false, doctors = doctors) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}