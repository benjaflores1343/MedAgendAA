package com.example.medagenda.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.network.LoginRequest
import com.example.medagenda.domain.repository.UsuarioRepository
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
    data class Success(val userName: String, val userId: Long) : LoginResult
    data class DoctorSuccess(val userName: String, val medicoId: Long) : LoginResult
}

sealed class LoginUiEvent {
    data class EmailChanged(val email: String) : LoginUiEvent()
    data class PasswordChanged(val password: String) : LoginUiEvent()
    object LoginAsPatient : LoginUiEvent()
    object LoginAsDoctor : LoginUiEvent()
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
            is LoginUiEvent.LoginAsPatient -> {
                submitData(isDoctor = false)
            }
            is LoginUiEvent.LoginAsDoctor -> {
                submitData(isDoctor = true)
            }
        }
    }

    private fun submitData(isDoctor: Boolean) {
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
            try {
                val loginRequest = LoginRequest(uiState.email, uiState.password)
                val user = usuarioRepository.login(loginRequest)

                if (isDoctor && user.tipo == "Medico") {
                    // The API doesn't return the medicoId, so for now we'll pass the userId.
                    // We might need to adjust this later.
                    resultChannel.send(LoginResult.DoctorSuccess(userName = user.nombre, medicoId = user.id))
                } else if (!isDoctor && user.tipo == "Paciente") {
                    // The API doesn't return the pacienteId, so for now we'll pass the userId.
                    // We might need to adjust this later.
                    resultChannel.send(LoginResult.Success(userName = user.nombre, userId = user.id))
                } else {
                    uiState = uiState.copy(authError = "Rol incorrecto para el tipo de inicio de sesión.")
                }

            } catch (e: Exception) {
                uiState = uiState.copy(authError = "Error de autenticación: ${e.message}")
            }
        }
    }
}