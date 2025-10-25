package com.example.medagenda.data.repository

import com.example.medagenda.data.local.dao.RolDao
import com.example.medagenda.data.local.dao.UsuarioDao
import com.example.medagenda.data.local.entity.Rol
import com.example.medagenda.data.local.entity.Usuario
import com.example.medagenda.data.local.entity.UsuarioRol
import com.example.medagenda.domain.repository.UsuarioRepository

class UsuarioRepositoryImpl(
    private val usuarioDao: UsuarioDao,
    private val rolDao: RolDao
) : UsuarioRepository {

    override suspend fun registerUser(usuario: Usuario) {
        // Insert user and get the new ID
        val newUserId = usuarioDao.insertUsuario(usuario)

        // Find the "Paciente" role
        val pacienteRol = rolDao.findRolByName("Paciente")

        // Assign the role to the new user
        pacienteRol?.let { rol ->
            val usuarioRol = UsuarioRol(
                idUsuario = newUserId,
                idRol = rol.idRol
            )
            rolDao.assignRolToUser(usuarioRol)
        }
    }

    override suspend fun findByEmail(email: String): Usuario? {
        return usuarioDao.findByEmail(email)
    }

    override suspend fun getRolForUser(idUsuario: Long): Rol? {
        return rolDao.getRolForUser(idUsuario)
    }
}