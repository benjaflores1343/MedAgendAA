package com.example.medagenda.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.medagenda.data.local.dao.RolDao
import com.example.medagenda.data.local.dao.UsuarioDao
import com.example.medagenda.data.local.entity.Rol
import com.example.medagenda.data.local.entity.Usuario
import com.example.medagenda.data.local.entity.UsuarioRol

@Database(
    entities = [Usuario::class, Rol::class, UsuarioRol::class],
    version = 2,
    exportSchema = false
)
abstract class MedAgendaDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun rolDao(): RolDao

}