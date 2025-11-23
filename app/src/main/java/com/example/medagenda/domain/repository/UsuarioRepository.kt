package com.example.medagenda.domain.repository

import com.example.medagenda.data.local.dto.*
import com.example.medagenda.data.local.entity.*
import com.example.medagenda.data.network.*
import kotlinx.coroutines.flow.Flow

interface UsuarioRepository {
    suspend fun login(loginRequest: LoginRequest): UserApiResponse
    suspend fun registerUser(registerRequest: RegisterRequest)
    suspend fun findByEmail(email: String): Usuario?
    suspend fun getAllEspecialidades(): List<EspecialidadApi>
    suspend fun getMedicosByEspecialidad(idEspecialidad: Long): List<MedicoApi>
    suspend fun getAvailableHorariosForMedico(idMedico: Long): List<HorarioApi>
    suspend fun createAppointment(createAppointmentRequest: CreateAppointmentRequest)
    suspend fun findPacienteByUserId(idUsuario: Long): Paciente?
    suspend fun getAppointmentsForPatient(patientId: Long): List<AppointmentApiResponse>
    suspend fun findMedicoByUserId(idUsuario: Long): Medico?
    suspend fun getAppointmentsForDoctor(medicoId: Long): List<DoctorAppointmentApiResponse>
    suspend fun getAppointmentDetails(citaId: Long): AppointmentDetailApiResponse
    fun getAllUsersWithRoles(): Flow<List<UserInfo>>
    suspend fun saveReceta(createRecetaRequest: CreateRecetaRequest)
    suspend fun getRecetasForPaciente(patientId: Long): List<RecetaApiResponse>
    suspend fun deleteRecetas(deleteRecetasRequest: DeleteRecetasRequest)
    suspend fun isTimeSlotAvailable(horarioId: Long): Boolean
    suspend fun updateAppointmentStatus(citaId: Long, newStatus: String)
}