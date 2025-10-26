package com.example.medagenda.data.local.dto

// DTO for the "My Appointments" screen. It holds all the necessary information.
data class AppointmentInfo(
    val idCita: Long,
    val fechaHoraInicio: Long,
    val nombreMedico: String,
    val apellidoMedico: String,
    val nombreEspecialidad: String,
    val estadoCita: String
)
