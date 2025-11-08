package com.example.medagenda.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.medagenda.data.local.dao.*
import com.example.medagenda.data.local.entity.*
import com.example.medagenda.domain.security.PasswordHasher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
            .build()
            INSTANCE = instance

            // Check and populate the database only if it's newly created
            val prefs = context.getSharedPreferences("MedAgendaPrefs", Context.MODE_PRIVATE)
            val isDatabasePopulated = prefs.getBoolean("isDatabasePopulated", false)
            if (!isDatabasePopulated) {
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(instance)
                    prefs.edit().putBoolean("isDatabasePopulated", true).apply()
                }
            }
            instance
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

        // --- Specialties ---
        val especialidades = listOf(
            Especialidad(nombreEspecialidad = "Medicina General", descripcion = "Atención primaria y cuidado general."),
            Especialidad(nombreEspecialidad = "Cardiología", descripcion = "Especialidad médica que se ocupa de las enfermedades del corazón."),
            Especialidad(nombreEspecialidad = "Dermatología", descripcion = "Tratamiento de enfermedades de la piel.")
        )
        especialidadDao.insertEspecialidades(especialidades)
        
        // Get roles and specialties back to get their generated IDs
        val medicoRol = rolDao.findRolByName("Médico")
        val medicinaGeneral = especialidadDao.findEspecialidadByName("Medicina General")
        val cardiologia = especialidadDao.findEspecialidadByName("Cardiología")

        // --- Doctors ---
        if (medicoRol != null && medicinaGeneral != null && cardiologia != null) {
            // General Medicine Doctor
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

            // Cardiologist
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
    }
}