package com.example.medagenda.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.medagenda.data.local.entity.Rol
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MedAgendaDatabaseProvider {
    private var INSTANCE: MedAgendaDatabase? = null

    fun getDatabase(context: Context): MedAgendaDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                MedAgendaDatabase::class.java,
                "medagenda_database"
            )
            .fallbackToDestructiveMigration() // Destroys and recreates the DB on version change
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Populate the database with initial data
                    INSTANCE?.let { database ->
                        CoroutineScope(Dispatchers.IO).launch {
                            val rolDao = database.rolDao()
                            rolDao.insertRoles(
                                listOf(
                                    Rol(nombreRol = "Administrador", descripcion = "Gestión total del sistema"),
                                    Rol(nombreRol = "Médico", descripcion = "Gestión de sus propias citas y pacientes"),
                                    Rol(nombreRol = "Recepcionista", descripcion = "Gestión de citas y pacientes"),
                                    Rol(nombreRol = "Paciente", descripcion = "Acceso a sus propias citas")
                                )
                            )
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