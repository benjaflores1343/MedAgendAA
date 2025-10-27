package com.example.medagenda.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.local.dto.MedicoInfo
import com.example.medagenda.data.repository.MedicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class SelectDoctorState(
    val isLoading: Boolean = true,
    val doctors: List<MedicoInfo> = emptyList(),
    val error: String? = null
)

class SelectDoctorViewModel(private val repository: MedicoRepository) : ViewModel() {

    private val _state = MutableStateFlow(SelectDoctorState())
    val state: StateFlow<SelectDoctorState> = _state.asStateFlow()

    fun loadDoctorsBySpecialty(specialtyId: Long) {
        viewModelScope.launch {
            _state.value = SelectDoctorState(isLoading = true)
            repository.getMedicosByEspecialidad(specialtyId)
                .catch { e ->
                    _state.value = SelectDoctorState(isLoading = false, error = e.message)
                }
                .collect { doctors ->
                    _state.value = SelectDoctorState(isLoading = false, doctors = doctors)
                }
        }
    }
}