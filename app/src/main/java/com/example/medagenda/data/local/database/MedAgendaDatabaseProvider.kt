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
        val rolDao = database.rolDao()
        rolDao.insertRoles(listOf(
            Rol(nombreRol = "Paciente", descripcion = "Acceso a sus propias citas")
        ))
    }
}