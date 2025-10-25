package com.example.medagenda.ui.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.local.dto.MedicoInfo
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class SelectDoctorVm(
    usuarioRepository: UsuarioRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val specialtyId = savedStateHandle.getStateFlow("specialtyId", -1L)

    val medicosState: StateFlow<List<MedicoInfo>> = specialtyId.flatMapLatest { id ->
        if (id != -1L) {
            usuarioRepository.getMedicosByEspecialidad(id)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = emptyList()
    )
}