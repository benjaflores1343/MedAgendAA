package com.example.medagenda.domain.repository

import com.example.medagenda.data.local.dto.AppointmentInfo
import com.example.medagenda.data.local.dto.MedicoInfo
import com.example.medagenda.data.local.entity.*
import kotlinx.coroutines.flow.Flow

interface UsuarioRepository {
    suspend fun registerUser(usuario: Usuario, fechaNacimiento: String, direccion: String)
    suspend fun findByEmail(email: String): Usuario?
    suspend fun getRolForUser(idUsuario: Long): Rol?
    fun getAllEspecialidades(): Flow<List<Especialidad>>
    fun getMedicosByEspecialidad(idEspecialidad: Long): Flow<List<MedicoInfo>>
    fun getAvailableHorariosForMedico(idMedico: Long): Flow<List<Horario>>
    suspend fun createAppointment(cita: Cita)
    suspend fun findPacienteByUserId(idUsuario: Long): Paciente?
    fun getAppointmentsForPatient(patientId: Long): Flow<List<AppointmentInfo>>
    suspend fun findMedicoByUserId(idUsuario: Long): Medico?
}