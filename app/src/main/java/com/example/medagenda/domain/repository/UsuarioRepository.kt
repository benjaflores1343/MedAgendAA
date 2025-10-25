package com.example.medagenda.domain.repository

import com.example.medagenda.data.local.entity.Rol
import com.example.medagenda.data.local.entity.Usuario

interface UsuarioRepository {
    suspend fun registerUser(usuario: Usuario, fechaNacimiento: String, direccion: String)
    suspend fun findByEmail(email: String): Usuario?
    suspend fun getRolForUser(idUsuario: Long): Rol?
}