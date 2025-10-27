package com.example.medagenda.ui.screen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.local.entity.Receta
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CameraState(
    val photoUri: Uri? = null,
    val isSaving: Boolean = false,
    val saveResult: Result<Unit>? = null
)

class CameraViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()

    fun onPhotoTaken(uri: Uri) {
        _state.value = _state.value.copy(photoUri = uri)
    }

    fun onRetakePhoto() {
        _state.value = _state.value.copy(photoUri = null)
    }

    fun saveReceta(pacienteId: Long) {
        val uri = state.value.photoUri ?: return
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true)
            try {
                val receta = Receta(idPaciente = pacienteId, uriFoto = uri.toString())
                repository.saveReceta(receta)
                _state.value = _state.value.copy(isSaving = false, saveResult = Result.success(Unit))
            } catch (e: Exception) {
                _state.value = _state.value.copy(isSaving = false, saveResult = Result.failure(e))
            }
        }
    }
}