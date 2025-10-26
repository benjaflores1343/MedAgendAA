package com.example.medagenda.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.local.dto.UserInfo
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

data class UserListState(
    val users: List<UserInfo> = emptyList(),
    val isLoading: Boolean = false
)

class UserListVm(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    private val _state = MutableStateFlow(UserListState())
    val state = _state.asStateFlow()

    init {
        getUsers()
    }

    private fun getUsers() {
        usuarioRepository.getAllUsersWithRoles()
            .onEach { users ->
                _state.update { it.copy(users = users, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }
}