package com.example.medagenda.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.domain.repository.UsuarioRepository
import com.example.medagenda.domain.security.PasswordHasher
import com.example.medagenda.domain.validation.ValidateEmail
import com.example.medagenda.domain.validation.ValidatePassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val authError: String? = null
)

sealed interface LoginResult {
    data class Success(val userName: String, val userRole: String, val pacienteId: Long) : LoginResult
}

sealed class LoginUiEvent {
    data class EmailChanged(val email: String) : LoginUiEvent()
    data class PasswordChanged(val password: String) : LoginUiEvent()
    object Login : LoginUiEvent()
}

class LoginScreenVm(
    private val usuarioRepository: UsuarioRepository,
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword()
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    private val resultChannel = Channel<LoginResult>()
    val loginResults = resultChannel.receiveAsFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EmailChanged -> {
                uiState = uiState.copy(email = event.email, emailError = null, authError = null)
            }
            is LoginUiEvent.PasswordChanged -> {
                uiState = uiState.copy(password = event.password, passwordError = null, authError = null)
            }
            is LoginUiEvent.Login -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val emailResult = validateEmail.execute(uiState.email)
        val passwordResult = validatePassword.execute(uiState.password)

        val hasError = listOf(emailResult, passwordResult).any { !it.successful }

        if (hasError) {
            uiState = uiState.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
            return
        }

        viewModelScope.launch {
            val user = usuarioRepository.findByEmail(uiState.email)
            if (user == null) {
                uiState = uiState.copy(authError = "Email o contraseña incorrectos")
                return@launch
            }

            val hashedPassword = PasswordHasher.hashPassword(uiState.password)
            if (hashedPassword != user.contrasenaHash) {
                uiState = uiState.copy(authError = "Email o contraseña incorrectos")
                return@launch
            }

            val rol = usuarioRepository.getRolForUser(user.idUsuario)
            val paciente = usuarioRepository.findPacienteByUserId(user.idUsuario)

            if (rol != null && paciente != null) {
                resultChannel.send(
                    LoginResult.Success(
                        userName = user.nombre,
                        userRole = rol.nombreRol,
                        pacienteId = paciente.idPaciente
                    )
                )
            } else {
                // Handle case where user is not a patient or role is not found
                uiState = uiState.copy(authError = "Este usuario no tiene un perfil de paciente.")
            }
        }
    }
}