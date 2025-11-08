package com.example.medagenda.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.medagenda.data.local.dao.*
import com.example.medagenda.data.local.entity.*
import com.example.medagenda.domain.security.PasswordHasher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.Calendar

object MedAgendaDatabaseProvider {
    @Volatile
    private var INSTANCE: MedAgendaDatabase? = null

    fun getDatabase(context: Context): MedAgendaDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                MedAgendaDatabase::class.java,
                "medagenda_database"
            )
            .fallbackToDestructiveMigration()
            .addCallback(RoomDbCallback(context.applicationContext))
            .build()
            INSTANCE = instance
            instance
        }
    }

    private class RoomDbCallback(private val context: Context) : RoomDatabase.Callback() {
        private val applicationScope = CoroutineScope(SupervisorJob())

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            applicationScope.launch(Dispatchers.IO) {
                populateDatabase(getDatabase(context))
            }
        }
    }

    private suspend fun populateDatabase(database: MedAgendaDatabase) {
        val usuarioDao = database.usuarioDao()
        val rolDao = database.rolDao()
        val especialidadDao = database.especialidadDao()
        val medicoDao = database.medicoDao()
        val horarioDao = database.horarioDao()

        // --- Roles ---
        val roles = listOf(
            Rol(nombreRol = "Médico", descripcion = "Gestión de sus propias citas y pacientes"),
            Rol(nombreRol = "Paciente", descripcion = "Acceso a sus propias citas")
        )
        rolDao.insertRoles(roles)

        // Get roles back to get their generated IDs
        val medicoRol = rolDao.findRolByName("Médico")

        // --- Specialties ---
        val especialidades = listOf(
            Especialidad(nombreEspecialidad = "Medicina General", descripcion = "Atención primaria y cuidado general."),
            Especialidad(nombreEspecialidad = "Cardiología", descripcion = "Especialidad médica que se ocupa de las enfermedades del corazón."),
            Especialidad(nombreEspecialidad = "Dermatología", descripcion = "Tratamiento de enfermedades de la piel.")
        )
        especialidadDao.insertEspecialidades(especialidades)
        val medicinaGeneral = especialidadDao.findEspecialidadByName("Medicina General")
        val cardiologia = especialidadDao.findEspecialidadByName("Cardiología")
        val dermatologia = especialidadDao.findEspecialidadByName("Dermatología")

        // --- Doctors ---
        if (medicoRol != null) {
            // General Medicine Doctor
            if (medicinaGeneral != null) {
                val drGarciaUser = Usuario(rut = "11.111.111-1", nombre = "Carlos", apellido = "García", email = "cgarcia@medagenda.com", telefono = "912345678", contrasenaHash = PasswordHasher.hashPassword("123456"))
                val idDrGarciaUsuario = usuarioDao.insertUsuario(drGarciaUser)
                rolDao.assignRolToUser(UsuarioRol(idUsuario = idDrGarciaUsuario, idRol = medicoRol.idRol))
                val idDrGarciaMedico = medicoDao.insertMedico(Medico(idUsuario = idDrGarciaUsuario, idEspecialidad = medicinaGeneral.idEspecialidad, biografia = "Especialista en atención primaria."))
                val horariosDrGarcia = mutableListOf<Horario>()
                for (i in 0..4) {
                    val dayCalendar = Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_YEAR, i + 1)
                        set(Calendar.HOUR_OF_DAY, 9 + i)
                        set(Calendar.MINUTE, 0)
                    }
                    horariosDrGarcia.add(Horario(idMedico = idDrGarciaMedico, fechaHoraInicio = dayCalendar.timeInMillis, fechaHoraFin = dayCalendar.timeInMillis + 3600000, estado = "Disponible"))
                }
                horarioDao.insertHorarios(horariosDrGarcia)
            }

            // Cardiologist
            if (cardiologia != null) {
                val draLopezUser = Usuario(rut = "22.222.222-2", nombre = "Ana", apellido = "López", email = "alopez@medagenda.com", telefono = "987654321", contrasenaHash = PasswordHasher.hashPassword("123456"))
                val idDraLopezUsuario = usuarioDao.insertUsuario(draLopezUser)
                rolDao.assignRolToUser(UsuarioRol(idUsuario = idDraLopezUsuario, idRol = medicoRol.idRol))
                val idDraLopezMedico = medicoDao.insertMedico(Medico(idUsuario = idDraLopezUsuario, idEspecialidad = cardiologia.idEspecialidad, biografia = "Cardióloga con 10 años de experiencia en enfermedades coronarias."))
                val horariosDraLopez = mutableListOf<Horario>()
                for (i in 1..3) { // Next 3 workdays
                    val dayCalendar = Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_YEAR, i)
                        set(Calendar.HOUR_OF_DAY, 10)
                        set(Calendar.MINUTE, 0)
                    }
                    horariosDraLopez.add(Horario(idMedico = idDraLopezMedico, fechaHoraInicio = dayCalendar.timeInMillis, fechaHoraFin = dayCalendar.timeInMillis + 3600000, estado = "Disponible"))
                }
                horarioDao.insertHorarios(horariosDraLopez)
            }

            // Dermatologist
            if (dermatologia != null) {
                val drPerezUser = Usuario(rut = "33.333.333-3", nombre = "Juan", apellido = "Pérez", email = "jperez@medagenda.com", telefono = "955512345", contrasenaHash = PasswordHasher.hashPassword("123456"))
                val idDrPerezUsuario = usuarioDao.insertUsuario(drPerezUser)
                rolDao.assignRolToUser(UsuarioRol(idUsuario = idDrPerezUsuario, idRol = medicoRol.idRol))
                val idDrPerezMedico = medicoDao.insertMedico(Medico(idUsuario = idDrPerezUsuario, idEspecialidad = dermatologia.idEspecialidad, biografia = "Dermatólogo especializado en acné y psoriasis."))
                val horariosDrPerez = mutableListOf<Horario>()
                for (i in 2..5) { // From the day after tomorrow for 4 days
                    val dayCalendar = Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_YEAR, i)
                        set(Calendar.HOUR_OF_DAY, 14)
                        set(Calendar.MINUTE, 30)
                    }
                    horariosDrPerez.add(Horario(idMedico = idDrPerezMedico, fechaHoraInicio = dayCalendar.timeInMillis, fechaHoraFin = dayCalendar.timeInMillis + 1800000, estado = "Disponible")) // 30 min slots
                }
                horarioDao.insertHorarios(horariosDrPerez)
            }
        }
    }
}