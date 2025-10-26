package com.example.medagenda.data.local.dto

// DTO for the Appointment Detail screen. It holds all the necessary information.
data class AppointmentFullDetails(
    val idCita: Long,
    val fechaHoraInicio: Long,
    val fechaHoraFin: Long,
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
