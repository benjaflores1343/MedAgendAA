package com.example.medagenda.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.medagenda.data.local.dao.EspecialidadDao
import com.example.medagenda.data.local.dao.MedicoDao
import com.example.medagenda.data.local.dao.PacienteDao
import com.example.medagenda.data.local.dao.RolDao
import com.example.medagenda.data.local.dao.UsuarioDao
import com.example.medagenda.data.local.entity.*

@Database(
    entities = [
        Usuario::class, Rol::class, UsuarioRol::class, Paciente::class, 
        Especialidad::class, Medico::class
    ],
    version = 4,
    exportSchema = false
)
abstract class MedAgendaDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun rolDao(): RolDao
    abstract fun pacienteDao(): PacienteDao
    abstract fun especialidadDao(): EspecialidadDao
    abstract fun medicoDao(): MedicoDao

}