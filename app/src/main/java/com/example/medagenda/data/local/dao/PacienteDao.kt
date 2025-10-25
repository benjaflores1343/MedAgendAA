package com.example.medagenda.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.medagenda.data.local.entity.Paciente

@Dao
interface PacienteDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPaciente(paciente: Paciente)
}