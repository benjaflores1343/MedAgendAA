package com.example.medagenda.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.local.entity.Especialidad
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class RequestAppointmentVm(
    usuarioRepository: UsuarioRepository
) : ViewModel() {

    val especialidadesState: StateFlow<List<Especialidad>> = usuarioRepository.getAllEspecialidades()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = emptyList()
        )
}