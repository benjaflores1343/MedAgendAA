package com.example.medagenda.ui.screen

import android.net.Uri

sealed class RegistrationFormEvent {
    data class NombreChanged(val nombre: String) : RegistrationFormEvent()
    data class ApellidoChanged(val apellido: String) : RegistrationFormEvent()
    data class RutChanged(val rut: String) : RegistrationFormEvent()
    data class TelefonoChanged(val telefono: String) : RegistrationFormEvent()
    data class FechaNacimientoChanged(val fechaNacimiento: String) : RegistrationFormEvent()
    data class DireccionChanged(val direccion: String) : RegistrationFormEvent()
    data class EmailChanged(val email: String) : RegistrationFormEvent()
    data class PasswordChanged(val password: String) : RegistrationFormEvent()
    data class RepeatedPasswordChanged(val repeatedPassword: String) : RegistrationFormEvent()
    data class AcceptTerms(val isAccepted: Boolean) : RegistrationFormEvent()
    data class ProfileImageChanged(val uri: Uri?) : RegistrationFormEvent()
    object Submit : RegistrationFormEvent()
}
