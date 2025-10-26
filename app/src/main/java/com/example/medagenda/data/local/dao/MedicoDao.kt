package com.example.medagenda.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.medagenda.data.local.dto.MedicoInfo
import com.example.medagenda.data.local.entity.Medico
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicoDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMedico(medico: Medico): Long // Returns the new doctor's ID

    @Query("SELECT * FROM medicos WHERE id_usuario = :userId LIMIT 1")
    suspend fun findMedicoByUserId(userId: Long): Medico?

    @Query("""
        SELECT m.id_medico AS idMedico, u.id_usuario AS idUsuario, u.nombre, u.apellido, m.biografia
        FROM medicos AS m
        INNER JOIN usuarios AS u ON m.id_usuario = u.id_usuario
        WHERE m.id_especialidad = :idEspecialidad
    """)
    fun getMedicosByEspecialidad(idEspecialidad: Long): Flow<List<MedicoInfo>>
}