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

            if (isDoctor) {
                // Doctor Login
                val medico = usuarioRepository.findMedicoByUserId(user.idUsuario)
                if (medico != null) {
                    resultChannel.send(LoginResult.DoctorSuccess(userName = user.nombre, medicoId = medico.idMedico))
                } else {
                    uiState = uiState.copy(authError = "Este usuario no tiene un perfil de médico.")
                }
            } else {
                // Patient Login: The HomeScreen will be responsible for finding the patient ID from the user ID.
                val paciente = usuarioRepository.findPacienteByUserId(user.idUsuario)
                if (paciente != null) {
                    resultChannel.send(LoginResult.Success(userName = user.nombre, userId = user.idUsuario))
                } else {
                    uiState = uiState.copy(authError = "Este usuario no tiene un perfil de paciente.")
                }
            }
        }
    }
}