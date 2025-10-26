package com.example.medagenda.data.local.dto

// DTO for the admin user list screen.
data class UserInfo(
    val idUsuario: Long,
    val nombre: String,
    val apellido: String,
    val email: String,
    val nombreRol: String
)
