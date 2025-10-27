package com.example.medagenda.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.local.entity.Receta
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class RecetasState(
    val isLoading: Boolean = true,
    val recetas: List<Receta> = emptyList(),
    val error: String? = null
)

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
                    _state.value = RecetasState(isLoading = false, recetas = recetas)
                }
        }
    }
}