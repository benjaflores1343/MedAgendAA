package com.example.medagenda.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.medagenda.data.local.dao.*
import com.example.medagenda.data.local.entity.*
import com.example.medagenda.domain.security.PasswordHasher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.Calendar

@Database(
    entities = [
        Usuario::class, Rol::class, UsuarioRol::class, Paciente::class,
        Especialidad::class, Medico::class, Horario::class, Cita::class,
        Receta::class
    ],
    version = 43, // Incremented to ensure recreation
    exportSchema = false
)
abstract class MedAgendaDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun rolDao(): RolDao
    abstract fun pacienteDao(): PacienteDao
    abstract fun especialidadDao(): EspecialidadDao
    abstract fun medicoDao(): MedicoDao
    abstract fun horarioDao(): HorarioDao
    abstract fun citaDao(): CitaDao
    abstract fun recetaDao(): RecetaDao

    companion object {
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
                .build()
                INSTANCE = instance

                // Check and populate the database only if it's newly created
                val prefs = context.getSharedPreferences("MedAgendaPrefs", Context.MODE_PRIVATE)
                val isDatabasePopulated = prefs.getBoolean("isDatabasePopulated_v43", false)
                if (!isDatabasePopulated) {
                    runBlocking(Dispatchers.IO) {
                        populateInitialData(instance)
                        prefs.edit().putBoolean("isDatabasePopulated_v43", true).apply()
                    }
                }
                instance
            }
        }

        private suspend fun populateInitialData(database: MedAgendaDatabase) {
            val usuarioDao = database.usuarioDao()
            val rolDao = database.rolDao()
            val especialidadDao = database.especialidadDao()
            val medicoDao = database.medicoDao()
            val horarioDao = database.horarioDao()

            // --- Roles ---
            rolDao.insertRoles(listOf(
                Rol(nombreRol = "Médico", descripcion = "Gestión de citas y pacientes"),
                Rol(nombreRol = "Paciente", descripcion = "Acceso a citas y perfil")
            ))

            // --- Specialties ---
            especialidadDao.insertEspecialidades(listOf(
                Especialidad(nombreEspecialidad = "Medicina General", descripcion = "Atención primaria."),
                Especialidad(nombreEspecialidad = "Cardiología", descripcion = "Corazón y vasos sanguíneos."),
                Especialidad(nombreEspecialidad = "Dermatología", descripcion = "Enfermedades de la piel.")
            ))

            val medicoRol = rolDao.findRolByName("Médico")
            val medicinaGeneral = especialidadDao.findEspecialidadByName("Medicina General")
            val cardiologia = especialidadDao.findEspecialidadByName("Cardiología")

            // --- Doctors ---
            if (medicoRol != null && medicinaGeneral != null && cardiologia != null) {
                // Dr. Garcia
                val drGarciaUser = Usuario(rut = "11.111.111-1", nombre = "Carlos", apellido = "García", email = "cgarcia@medagenda.com", telefono = "912345678", contrasenaHash = PasswordHasher.hashPassword("123456"))
                val idDrGarciaUsuario = usuarioDao.insertUsuario(drGarciaUser)
                rolDao.assignRolToUser(UsuarioRol(idUsuario = idDrGarciaUsuario, idRol = medicoRol.idRol))
                val idDrGarciaMedico = medicoDao.insertMedico(Medico(idUsuario = idDrGarciaUsuario, idEspecialidad = medicinaGeneral.idEspecialidad, biografia = "Especialista en atención primaria."))
                (0..4).forEach { i ->
                    val day = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, i + 1); set(Calendar.HOUR_OF_DAY, 9 + i); set(Calendar.MINUTE, 0) }
                    horarioDao.insertHorarios(listOf(Horario(idMedico = idDrGarciaMedico, fechaHoraInicio = day.timeInMillis, fechaHoraFin = day.timeInMillis + 3600000, estado = "Disponible")))
                }

                // Dra. Lopez
                val draLopezUser = Usuario(rut = "22.222.222-2", nombre = "Ana", apellido = "López", email = "alopez@medagenda.com", telefono = "987654321", contrasenaHash = PasswordHasher.hashPassword("123456"))
                val idDraLopezUsuario = usuarioDao.insertUsuario(draLopezUser)
                rolDao.assignRolToUser(UsuarioRol(idUsuario = idDraLopezUsuario, idRol = medicoRol.idRol))
                val idDraLopezMedico = medicoDao.insertMedico(Medico(idUsuario = idDraLopezUsuario, idEspecialidad = cardiologia.idEspecialidad, biografia = "Cardióloga con 10 años de experiencia."))
                (1..3).forEach { i ->
                     val day = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, i); set(Calendar.HOUR_OF_DAY, 10); set(Calendar.MINUTE, 0) }
                    horarioDao.insertHorarios(listOf(Horario(idMedico = idDraLopezMedico, fechaHoraInicio = day.timeInMillis, fechaHoraFin = day.timeInMillis + 3600000, estado = "Disponible")))
                }
            }
        }
    }
}