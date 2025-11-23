package com.example.medagenda.data.repository

import com.example.medagenda.data.local.dao.*
import com.example.medagenda.data.local.dto.*
import com.example.medagenda.data.local.entity.*
import com.example.medagenda.data.network.*
import com.example.medagenda.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.Flow

class UsuarioRepositoryImpl(
    private val usuarioDao: UsuarioDao,
    private val rolDao: RolDao,
    private val pacienteDao: PacienteDao,
    private val especialidadDao: EspecialidadDao,
    private val medicoDao: MedicoDao,
    private val horarioDao: HorarioDao,
    private val citaDao: CitaDao,
    private val recetaDao: RecetaDao
) : UsuarioRepository {

    override suspend fun login(loginRequest: LoginRequest): UserApiResponse {
        return RetrofitClient.usuarios.login(loginRequest)
    }

    override suspend fun registerUser(registerRequest: RegisterRequest) {
        RetrofitClient.usuarios.register(registerRequest)
    }

    override suspend fun findByEmail(email: String): Usuario? {
        // This method will no longer be used for login, but might be useful for other purposes.
        return usuarioDao.findByEmail(email)
    }

    override suspend fun getAllEspecialidades(): List<EspecialidadApi> {
        return RetrofitClient.citas.getEspecialidades()
    }

    override suspend fun getMedicosByEspecialidad(idEspecialidad: Long): List<MedicoApi> {
        return RetrofitClient.citas.getMedicosPorEspecialidad(idEspecialidad)
    }

    override suspend fun getAvailableHorariosForMedico(idMedico: Long): List<HorarioApi> {
        return RetrofitClient.citas.getHorariosDisponibles(idMedico)
    }

    override suspend fun createAppointment(createAppointmentRequest: CreateAppointmentRequest) {
        RetrofitClient.citas.createAppointment(createAppointmentRequest)
    }

    override suspend fun findPacienteByUserId(idUsuario: Long): Paciente? {
        return pacienteDao.findPacienteByUserId(idUsuario)
    }

    override suspend fun getAppointmentsForPatient(patientId: Long): List<AppointmentApiResponse> {
        return RetrofitClient.citas.getAppointmentsForPatient(patientId)
    }

    override suspend fun findMedicoByUserId(idUsuario: Long): Medico? {
        return medicoDao.findMedicoByUserId(idUsuario)
    }

    override fun getAppointmentsForDoctor(medicoId: Long): Flow<List<DoctorAppointmentInfo>> {
        return citaDao.getAppointmentsForDoctor(medicoId)
    }

    override suspend fun getAppointmentDetails(citaId: Long): AppointmentFullDetails? {
        return citaDao.getAppointmentDetails(citaId)
    }

    override fun getAllUsersWithRoles(): Flow<List<UserInfo>> {
        return usuarioDao.getAllUsersWithRoles()
    }

    override suspend fun saveReceta(receta: Receta) {
        recetaDao.insertReceta(receta)
    }

    override fun getRecetasForPaciente(idPaciente: Long): Flow<List<Receta>> {
        return recetaDao.getRecetasForPaciente(idPaciente)
    }

    override suspend fun deleteRecetas(recetaIds: List<Long>) {
        recetaDao.deleteRecetasByIds(recetaIds)
    }

    override suspend fun isTimeSlotAvailable(horarioId: Long): Boolean {
        // This will be handled by the backend now, so we can assume it's always available
        // until we implement the corresponding API endpoint.
        return true 
    }

    override suspend fun updateAppointmentStatus(citaId: Long, newStatus: String) {
        citaDao.updateAppointmentStatus(citaId, newStatus)
    }
}