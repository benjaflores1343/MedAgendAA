package com.example.medagenda.data.local.dto

data class MedicoInfo(
    val idMedico: Long,
    val idUsuario: Long,
    val nombre: String,
    val apellido: String,
    val biografia: String?
)
