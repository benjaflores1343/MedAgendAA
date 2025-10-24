package com.example.medagenda.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.medagenda.data.local.dao.UsuarioDao
import com.example.medagenda.data.local.entity.Usuario

@Database(
    entities = [Usuario::class],
    version = 1,
    exportSchema = false
)
abstract class MedAgendaDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

}