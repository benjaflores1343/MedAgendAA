package com.example.medagenda.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.medagenda.data.local.entity.Receta
import kotlinx.coroutines.flow.Flow

@Dao
interface RecetaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceta(receta: Receta)

    @Query("SELECT * FROM recetas WHERE id_paciente = :idPaciente ORDER BY fecha_creacion DESC")
    fun getRecetasForPaciente(idPaciente: Long): Flow<List<Receta>>

    @Query("DELETE FROM recetas WHERE id_receta IN (:recetaIds)")
    suspend fun deleteRecetasByIds(recetaIds: List<Long>)
}