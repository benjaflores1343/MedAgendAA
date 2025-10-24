package com.example.medagenda.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.domain.validation.ValidateEmail
import com.example.medagenda.domain.validation.ValidatePassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null
)

sealed class LoginUiEvent {
    data class EmailChanged(val email: String) : LoginUiEvent()
    data class PasswordChanged(val password: String) : LoginUiEvent()
    object Login : LoginUiEvent()
}

class LoginScreenVm(
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword()
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EmailChanged -> {
                uiState = uiState.copy(email = event.email, emailError = null)
            }
            is LoginUiEvent.PasswordChanged -> {
                uiState = uiState.copy(password = event.password, passwordError = null)
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
            validationEventChannel.send(ValidationEvent.Success)
        }
    }
}