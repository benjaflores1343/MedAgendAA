package com.example.medagenda.ui.screen

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medagenda.data.local.entity.Usuario
import com.example.medagenda.domain.repository.UsuarioRepository
import com.example.medagenda.domain.validation.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class RegistrationFormState(
    val nombre: String = "",
    val nombreError: String? = null,
    val apellido: String = "",
    val apellidoError: String? = null,
    val rut: String = "",
    val rutError: String? = null,
    val telefono: String = "",
    val telefonoError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val repeatedPassword: String = "",
    val repeatedPasswordError: String? = null,
    val acceptedTerms: Boolean = false,
    val termsError: String? = null,
    val profileImageUri: Uri? = null
)

sealed class ValidationEvent {
    object Success : ValidationEvent()
}

class RegisterScreenVm(
    private val usuarioRepository: UsuarioRepository,
    private val validateNombre: ValidateNombre = ValidateNombre(),
    private val validateApellido: ValidateApellido = ValidateApellido(),
    private val validateRut: ValidateRut = ValidateRut(),
    private val validateTelefono: ValidateTelefono = ValidateTelefono(),
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val validateRepeatedPassword: ValidateRepeatedPassword = ValidateRepeatedPassword(),
    private val validateTerms: ValidateTerms = ValidateTerms()
) : ViewModel() {

    var state by mutableStateOf(RegistrationFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: RegistrationFormEvent) {
        when (event) {
            is RegistrationFormEvent.NombreChanged -> {
                state = state.copy(nombre = event.nombre)
            }
            is RegistrationFormEvent.ApellidoChanged -> {
                state = state.copy(apellido = event.apellido)
            }
            is RegistrationFormEvent.RutChanged -> {
                state = state.copy(rut = event.rut)
            }
            is RegistrationFormEvent.TelefonoChanged -> {
                state = state.copy(telefono = event.telefono)
            }
            is RegistrationFormEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }
            is RegistrationFormEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }
            is RegistrationFormEvent.RepeatedPasswordChanged -> {
                state = state.copy(repeatedPassword = event.repeatedPassword)
            }
            is RegistrationFormEvent.AcceptTerms -> {
                state = state.copy(acceptedTerms = event.isAccepted)
            }
            is RegistrationFormEvent.ProfileImageChanged -> {
                state = state.copy(profileImageUri = event.uri)
            }
            is RegistrationFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val nombreResult = validateNombre.execute(state.nombre)
        val apellidoResult = validateApellido.execute(state.apellido)
        val rutResult = validateRut.execute(state.rut)
        val telefonoResult = validateTelefono.execute(state.telefono)
        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password)
        val repeatedPasswordResult = validateRepeatedPassword.execute(
            state.password,
            state.repeatedPassword
        )
        val termsResult = validateTerms.execute(state.acceptedTerms)

        val hasError = listOf(
            nombreResult,
            apellidoResult,
            rutResult,
            telefonoResult,
            emailResult,
            passwordResult,
            repeatedPasswordResult,
            termsResult
        ).any { !it.successful }

        if (hasError) {
            state = state.copy(
                nombreError = nombreResult.errorMessage,
                apellidoError = apellidoResult.errorMessage,
                rutError = rutResult.errorMessage,
                telefonoError = telefonoResult.errorMessage,
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                repeatedPasswordError = repeatedPasswordResult.errorMessage,
                termsError = termsResult.errorMessage
            )
            return
        }
        
        viewModelScope.launch {
            val newUser = Usuario(
                rut = state.rut,
                nombre = state.nombre,
                apellido = state.apellido,
                email = state.email,
                telefono = state.telefono,
                contrasenaHash = state.password // TODO: Hash the password
            )
            usuarioRepository.registerUser(newUser)
            validationEventChannel.send(ValidationEvent.Success)
        }
    }
}

sealed class RegistrationFormEvent {
    data class NombreChanged(val nombre: String) : RegistrationFormEvent()
    data class ApellidoChanged(val apellido: String) : RegistrationFormEvent()
    data class RutChanged(val rut: String) : RegistrationFormEvent()
    data class TelefonoChanged(val telefono: String) : RegistrationFormEvent()
    data class EmailChanged(val email: String) : RegistrationFormEvent()
    data class PasswordChanged(val password: String) : RegistrationFormEvent()
    data class RepeatedPasswordChanged(val repeatedPassword: String) : RegistrationFormEvent()
    data class AcceptTerms(val isAccepted: Boolean) : RegistrationFormEvent()
    data class ProfileImageChanged(val uri: Uri?) : RegistrationFormEvent()
    object Submit : RegistrationFormEvent()
}
