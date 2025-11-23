package com.example.medagenda.data.network

/**
 * Data class representing the user information returned from the API.
 */
data class UserApiResponse(
    val id: Long,
    val nombre: String,
    val email: String,
    val tipo: String // "Medico" or "Paciente"
)

/**
 * Data class for the login request body.
 */
data class LoginRequest(
    val email: String,
    val contrasena: String
)

/**
 * Data class for the registration request body.
 */
data class RegisterRequest(
    val nombre: String,
    val apellido: String,
    val rut: String,
    val telefono: String,
    val fechaNacimiento: String,
    val direccion: String,
    val email: String,
    val contrasena: String
)

/**
 * Data class for the specialty information from the API.
 */
data class EspecialidadApi(
    val idEspecialidad: Long,
    val nombreEspecialidad: String,
    val descripcion: String
)

/**
 * Data class for the doctor information from the API.
 */
data class MedicoApi(
    val idMedico: Long,
    val nombre: String,
    val apellido: String,
    val biografia: String
)

/**
 * Data class for the time slot information from the API.
 */
data class HorarioApi(
    val idHorario: Long,
    val idMedico: Long,
    val fechaHoraInicio: String,
    val fechaHoraFin: String,
    val estado: String
)

/**
 * Data class for the create appointment request body.
 */
data class CreateAppointmentRequest(
    val idPaciente: Long,
    val idMedico: Long,
    val idHorario: Long
)

/**
 * Data class for the patient's appointment information from the API.
 */
data class AppointmentApiResponse(
    val idCita: Long,
    val fechaHoraInicio: String,
    val nombreMedico: String,
    val apellidoMedico: String,
    val nombreEspecialidad: String,
    val estadoCita: String
)

/**
 * Data class for the doctor's appointment information from the API.
 */
data class DoctorAppointmentApiResponse(
    val idCita: Long,
    val fechaHoraInicio: String,
    val nombrePaciente: String,
    val apellidoPaciente: String,
    val estadoCita: String
)

/**
 * Data class for updating the appointment status.
 */
data class UpdateAppointmentStatusRequest(
    val estado: String
)
