package com.example.medagenda.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.medagenda.data.local.dao.*
import com.example.medagenda.data.local.entity.*

@Database(
    entities = [
        Usuario::class, Rol::class, UsuarioRol::class, Paciente::class, 
        Especialidad::class, Medico::class, Horario::class, Cita::class
    ],
    version = 9, // Incremented version for the schema change
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

}