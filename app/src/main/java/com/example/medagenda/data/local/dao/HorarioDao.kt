package com.example.medagenda.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.medagenda.data.local.entity.Horario
import kotlinx.coroutines.flow.Flow

@Dao
interface HorarioDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHorarios(horarios: List<Horario>)

    @Query("SELECT * FROM horarios WHERE id_medico = :idMedico AND estado = 'Disponible' ORDER BY fecha_hora_inicio ASC")
    fun getAvailableHorariosForMedico(idMedico: Long): Flow<List<Horario>>
}