package com.example.medagenda.domain.repository

import com.example.medagenda.data.local.entity.Usuario

interface UsuarioRepository {
    suspend fun registerUser(usuario: Usuario)
    suspend fun findByEmail(email: String): Usuario?
}