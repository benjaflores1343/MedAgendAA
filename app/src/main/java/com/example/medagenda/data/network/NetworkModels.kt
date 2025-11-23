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
    val contrasena: String,
    val tipo: String = "Paciente" // Added mandatory field
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

/**
 * Data class for the full appointment details from the API.
 */
data class AppointmentDetailApiResponse(
    val idCita: Long,
    val fechaHoraInicio: String,
    val fechaHoraFin: String,
    val estadoCita: String,
    val nombrePaciente: String,
    val apellidoPaciente: String,
    val emailPaciente: String,
    val telefonoPaciente: String,
    val nombreMedico: String,
    val apellidoMedico: String,
    val emailMedico: String,
    val nombreEspecialidad: String
)

// --- Recipe Models ---

/**
 * Data class for the recipe information from the API.
 */
data class RecetaApiResponse(
    val idReceta: Long,
    val idPaciente: Long,
    val uriFoto: String,
    val fechaCreacion: String
)

/**
 * Data class for creating a new recipe.
 */
data class CreateRecetaRequest(
    val idPaciente: Long,
    val uriFoto: String
)

/**
 * Data class for deleting recipes.
 */
data class DeleteRecetasRequest(
    val recetaIds: List<Long>
)
