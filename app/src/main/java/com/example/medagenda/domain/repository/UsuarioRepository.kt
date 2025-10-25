package com.example.medagenda.domain.repository

import com.example.medagenda.data.local.dto.MedicoInfo
import com.example.medagenda.data.local.entity.Especialidad
import com.example.medagenda.data.local.entity.Rol
import com.example.medagenda.data.local.entity.Usuario
import kotlinx.coroutines.flow.Flow

interface UsuarioRepository {
    suspend fun registerUser(usuario: Usuario, fechaNacimiento: String, direccion: String)
    suspend fun findByEmail(email: String): Usuario?
    suspend fun getRolForUser(idUsuario: Long): Rol?
    fun getAllEspecialidades(): Flow<List<Especialidad>>
    fun getMedicosByEspecialidad(idEspecialidad: Long): Flow<List<MedicoInfo>>
}