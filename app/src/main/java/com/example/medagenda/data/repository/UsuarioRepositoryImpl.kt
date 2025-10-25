package com.example.medagenda.data.repository

import com.example.medagenda.data.local.dao.EspecialidadDao
import com.example.medagenda.data.local.dao.PacienteDao
import com.example.medagenda.data.local.dao.RolDao
import com.example.medagenda.data.local.dao.UsuarioDao
import com.example.medagenda.data.local.entity.Especialidad
import com.example.medagenda.data.local.entity.Paciente
import com.example.medagenda.data.local.entity.Rol
import com.example.medagenda.data.local.entity.Usuario
import com.example.medagenda.data.local.entity.UsuarioRol
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.Flow

class UsuarioRepositoryImpl(
    private val usuarioDao: UsuarioDao,
    private val rolDao: RolDao,
    private val pacienteDao: PacienteDao,
    private val especialidadDao: EspecialidadDao
) : UsuarioRepository {

    override suspend fun registerUser(usuario: Usuario, fechaNacimiento: String, direccion: String) {
        // 1. Insert user and get the new ID
        val newUserId = usuarioDao.insertUsuario(usuario)

        // 2. Find the "Paciente" role
        val pacienteRol = rolDao.findRolByName("Paciente")

        // 3. Assign the role to the new user
        pacienteRol?.let { rol ->
            val usuarioRol = UsuarioRol(
                idUsuario = newUserId,
                idRol = rol.idRol
            )
            rolDao.assignRolToUser(usuarioRol)
        }

        // 4. Create the patient profile with all the data
        val newPaciente = Paciente(
            idUsuario = newUserId,
            fechaNacimiento = fechaNacimiento,
            direccion = direccion
        )
        pacienteDao.insertPaciente(newPaciente)
    }

    override suspend fun findByEmail(email: String): Usuario? {
        return usuarioDao.findByEmail(email)
    }

    override suspend fun getRolForUser(idUsuario: Long): Rol? {
        return rolDao.getRolForUser(idUsuario)
    }

    override fun getAllEspecialidades(): Flow<List<Especialidad>> {
        return especialidadDao.getAllEspecialidades()
    }
}