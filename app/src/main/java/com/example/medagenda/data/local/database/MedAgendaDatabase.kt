package com.example.medagenda.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.medagenda.data.local.dao.*
import com.example.medagenda.data.local.entity.*
import com.example.medagenda.domain.security.PasswordHasher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Usuario::class, Rol::class, UsuarioRol::class, Paciente::class,
        Especialidad::class, Medico::class, Horario::class, Cita::class,
        Receta::class
    ],
    version = 35, // Incremented version
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
                .addCallback(DatabaseCallback(context))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    prepopulate(database)
                }
            }
        }

        suspend fun prepopulate(database: MedAgendaDatabase) {
            val rolDao = database.rolDao()
            val usuarioDao = database.usuarioDao()
            val medicoDao = database.medicoDao()
            val especialidadDao = database.especialidadDao()

            // Create and insert roles
            rolDao.insertRoles(listOf(
                Rol(nombreRol = "Medico", descripcion = "Acceso para personal médico."),
                Rol(nombreRol = "Paciente", descripcion = "Acceso para pacientes.")
            ))

            // Create and insert a specialty
            especialidadDao.insertEspecialidades(listOf(
                Especialidad(nombreEspecialidad = "Cardiología", descripcion = "Especialidad de cardiología.")
            ))

            val medicoRole = rolDao.findRolByName("Medico")
            val cardiologia = especialidadDao.findEspecialidadByName("Cardiología")

            if (medicoRole != null && cardiologia != null) {
                val idRolMedico = medicoRole.idRol
                val idEspecialidad = cardiologia.idEspecialidad

                // Create a test doctor user
                val doctorUser = Usuario(
                    nombre = "Dr. Carlos",
                    apellido = "Gonzalez",
                    rut = "12345678-9",
                    telefono = "987654321",
                    email = "medico@medagenda.com",
                    contrasenaHash = PasswordHasher.hashPassword("password123")
                )
                val idUsuarioDoctor = usuarioDao.insertUsuario(doctorUser)

                // Assign role to the user
                rolDao.assignRolToUser(UsuarioRol(idUsuario = idUsuarioDoctor, idRol = idRolMedico))

                // Create the doctor profile
                val medico = Medico(
                    idUsuario = idUsuarioDoctor,
                    idEspecialidad = idEspecialidad,
                    biografia = "Cardiólogo con 10 años de experiencia."
                )
                medicoDao.insertMedico(medico)
            }
        }
    }
}