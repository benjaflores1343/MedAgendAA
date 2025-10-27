package com.example.medagenda.data.repository

import com.example.medagenda.data.local.dao.*
import com.example.medagenda.data.local.dto.*
import com.example.medagenda.data.local.entity.*
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
    private val recetaDao: RecetaDao // Added RecetaDao
) : UsuarioRepository {

    override suspend fun registerUser(usuario: Usuario, fechaNacimiento: String, direccion: String) {
        val newUserId = usuarioDao.insertUsuario(usuario)
        val pacienteRol = rolDao.findRolByName("Paciente")
        pacienteRol?.let {
            rolDao.assignRolToUser(UsuarioRol(idUsuario = newUserId, idRol = it.idRol))
        }
        val newPaciente = Paciente(idUsuario = newUserId, fechaNacimiento = fechaNacimiento, direccion = direccion)
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

    override fun getMedicosByEspecialidad(idEspecialidad: Long): Flow<List<MedicoInfo>> {
        return medicoDao.getMedicosByEspecialidad(idEspecialidad)
    }

    override fun getAvailableHorariosForMedico(idMedico: Long): Flow<List<Horario>> {
        return horarioDao.getAvailableHorariosForMedico(idMedico)
    }

    override suspend fun createAppointment(cita: Cita) {
        citaDao.createAppointment(cita)
    }

    override suspend fun findPacienteByUserId(idUsuario: Long): Paciente? {
        return pacienteDao.findPacienteByUserId(idUsuario)
    }

    override fun getAppointmentsForPatient(patientId: Long): Flow<List<AppointmentInfo>> {
        return citaDao.getAppointmentsForPatient(patientId)
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
}