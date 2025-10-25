package com.example.medagenda.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.medagenda.data.local.entity.Especialidad
import kotlinx.coroutines.flow.Flow

@Dao
interface EspecialidadDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEspecialidades(especialidades: List<Especialidad>)

    @Query("SELECT * FROM especialidades ORDER BY nombre_especialidad ASC")
    fun getAllEspecialidades(): Flow<List<Especialidad>>
}