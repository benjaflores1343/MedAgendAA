package com.example.medagenda.data.local.dto

// DTO for the doctor's appointment list.
data class DoctorAppointmentInfo(
    val idCita: Long,
    val fechaHoraInicio: Long,
    val nombrePaciente: String,
    val apellidoPaciente: String,
    val estadoCita: String
)
