package com.example.medagenda.data.network

// Represents the user object returned by the API
data class UsuarioApi(
    val id: Long,
    val nombre: String,
    val email: String,
    val tipo: String // "Medico" or "Paciente"
)

// Represents the request body for the login endpoint
data class LoginRequest(
    val email: String,
    val contrasena: String
)
