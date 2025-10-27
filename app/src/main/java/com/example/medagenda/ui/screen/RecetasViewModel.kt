package com.example.medagenda.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.local.entity.Receta
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RecetasState(
    val isLoading: Boolean = true,
    val recetas: List<Receta> = emptyList(),
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
            _state.value = RecetasState(isLoading = true)
            repository.getRecetasForPaciente(pacienteId)
                .catch { e ->
                    _state.value = RecetasState(isLoading = false, error = e.message)
                }
                .collect { recetas ->
                    _state.update { it.copy(isLoading = false, recetas = recetas) }
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
            val idsToDelete = _state.value.selectedRecetaIds.toList()
            repository.deleteRecetas(idsToDelete)
            _state.update { it.copy(selectedRecetaIds = emptySet()) } // Clear selection after deletion
        }
    }
}