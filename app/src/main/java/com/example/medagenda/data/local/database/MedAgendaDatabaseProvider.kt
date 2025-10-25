package com.example.medagenda.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.medagenda.data.local.entity.*
import com.example.medagenda.domain.security.PasswordHasher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

object MedAgendaDatabaseProvider {
    private var INSTANCE: MedAgendaDatabase? = null

    fun getDatabase(context: Context): MedAgendaDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                MedAgendaDatabase::class.java,
                "medagenda_database"
            )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    INSTANCE?.let { database ->
                        CoroutineScope(Dispatchers.IO).launch {
                            // DAOs
                            val usuarioDao = database.usuarioDao()
                            val rolDao = database.rolDao()
                            val especialidadDao = database.especialidadDao()
                            val medicoDao = database.medicoDao()
                            val horarioDao = database.horarioDao()

                            // Insert roles
                            rolDao.insertRoles(listOf(
                                Rol(nombreRol = "Administrador", descripcion = "Gestión total del sistema"),
                                Rol(nombreRol = "Médico", descripcion = "Gestión de sus propias citas y pacientes"),
                                Rol(nombreRol = "Recepcionista", descripcion = "Gestión de citas y pacientes"),
                                Rol(nombreRol = "Paciente", descripcion = "Acceso a sus propias citas")
                            ))
                            val medicoRol = rolDao.findRolByName("Médico")

                            // Insert specialties
                            especialidadDao.insertEspecialidades(listOf(
                                Especialidad(nombreEspecialidad = "Medicina General"),
                                Especialidad(nombreEspecialidad = "Cardiología"),
                                Especialidad(nombreEspecialidad = "Dermatología")
                            ))
                            val medicinaGeneral = especialidadDao.findEspecialidadByName("Medicina General")
                            val cardiologia = especialidadDao.findEspecialidadByName("Cardiología")

                            // Create Doctors and their schedules
                            if (medicoRol != null) {
                                // Doctor 1
                                val drGarciaUser = Usuario(rut = "11.111.111-1", nombre = "Carlos", apellido = "García", email = "cgarcia@medagenda.com", telefono = "912345678", contrasenaHash = PasswordHasher.hashPassword("123456"))
                                val idDrGarciaUsuario = usuarioDao.insertUsuario(drGarciaUser)
                                rolDao.assignRolToUser(UsuarioRol(idUsuario = idDrGarciaUsuario, idRol = medicoRol.idRol))
                                if (medicinaGeneral != null) {
                                    val idDrGarciaMedico = medicoDao.insertMedico(Medico(idUsuario = idDrGarciaUsuario, idEspecialidad = medicinaGeneral.idEspecialidad, biografia = "Especialista en atención primaria."))
                                    val calendar = Calendar.getInstance()
                                    val horariosDrGarcia = mutableListOf<Horario>()
                                    for (i in 0..4) { // Next 5 days
                                        calendar.add(Calendar.DAY_OF_YEAR, 1)
                                        horariosDrGarcia.add(Horario(idMedico = idDrGarciaMedico, fechaHoraInicio = calendar.timeInMillis, fechaHoraFin = calendar.timeInMillis + 3600000)) // 1 hour slot
                                    }
                                    horarioDao.insertHorarios(horariosDrGarcia)
                                }

                                // Doctor 2
                                val draLopezUser = Usuario(rut = "22.222.222-2", nombre = "Ana", apellido = "López", email = "alopez@medagenda.com", telefono = "987654321", contrasenaHash = PasswordHasher.hashPassword("123456"))
                                val idDraLopezUsuario = usuarioDao.insertUsuario(draLopezUser)
                                rolDao.assignRolToUser(UsuarioRol(idUsuario = idDraLopezUsuario, idRol = medicoRol.idRol))
                                if (cardiologia != null) {
                                    val idDraLopezMedico = medicoDao.insertMedico(Medico(idUsuario = idDraLopezUsuario, idEspecialidad = cardiologia.idEspecialidad, biografia = "Cardióloga con 10 años de experiencia."))
                                    val calendar = Calendar.getInstance()
                                    val horariosDraLopez = mutableListOf<Horario>()
                                    for (i in 0..2) { // Next 3 days
                                        calendar.add(Calendar.DAY_OF_YEAR, 1)
                                        horariosDraLopez.add(Horario(idMedico = idDraLopezMedico, fechaHoraInicio = calendar.timeInMillis, fechaHoraFin = calendar.timeInMillis + 3600000)) // 1 hour slot
                                    }
                                    horarioDao.insertHorarios(horariosDraLopez)
                                }
                            }
                        }
                    }
                }
            })
            .build()
            INSTANCE = instance
            instance
        }
    }
}