package com.example.medagenda.data.local.database

import android.content.Context
import androidx.room.Room

object MedAgendaDatabaseProvider {
    private var INSTANCE: MedAgendaDatabase? = null

    fun getDatabase(context: Context): MedAgendaDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                MedAgendaDatabase::class.java,
                "medagenda_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}