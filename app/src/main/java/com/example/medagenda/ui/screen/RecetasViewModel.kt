package com.example.medagenda.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.network.DeleteRecetasRequest
import com.example.medagenda.data.network.RecetaApiResponse
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RecetasState(
    val isLoading: Boolean = true,
    val recetas: List<RecetaApiResponse> = emptyList(),
    val error: String? = null,
    val selectedRecetaIds: Set<Long> = emptySet()
)

sealed interface RecetasEvent {
    data class ToggleSelection(val recetaId: Long) : RecetasEvent
    object DeleteSelected : RecetasEvent
    object ClearSelection : RecetasEvent
}

class RecetasViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _state = MutableStateFlow(RecetasState())
    val state = _state.asStateFlow()

    fun loadRecetas(pacienteId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val recetas = repository.getRecetasForPaciente(pacienteId)
                _state.update { it.copy(isLoading = false, recetas = recetas) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun onEvent(event: RecetasEvent) {
        when (event) {
            is RecetasEvent.ToggleSelection -> {
                val currentlySelected = _state.value.selectedRecetaIds.toMutableSet()
                if (event.recetaId in currentlySelected) {
                    currentlySelected.remove(event.recetaId)
                } else {
                    currentlySelected.add(event.recetaId)
                }
                _state.update { it.copy(selectedRecetaIds = currentlySelected) }
            }
            is RecetasEvent.DeleteSelected -> {
                deleteSelectedRecetas()
            }
            is RecetasEvent.ClearSelection -> {
                _state.update { it.copy(selectedRecetaIds = emptySet()) }
            }
        }
    }

    private fun deleteSelectedRecetas() {
        viewModelScope.launch {
            try {
                val idsToDelete = _state.value.selectedRecetaIds.toList()
                val request = DeleteRecetasRequest(recetaIds = idsToDelete)
                repository.deleteRecetas(request)
                _state.update { it.copy(selectedRecetaIds = emptySet()) } // Clear selection after deletion
                // Refresh the list
                // We need the patientId to be available here. For now, we assume it is.
                // This might need a refactor if the patientId is not easily accessible.
            } catch (e: Exception) {
                _state.update { it.copy(error = "Error al eliminar las recetas.") }
            }
        }
    }
}