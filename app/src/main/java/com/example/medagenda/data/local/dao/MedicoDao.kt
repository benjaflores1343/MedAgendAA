package com.example.medagenda.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.medagenda.data.local.dto.MedicoInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicoDao {

    @Query("""
        SELECT m.id_medico AS idMedico, u.id_usuario AS idUsuario, u.nombre, u.apellido, m.biografia
        FROM medicos AS m
        INNER JOIN usuarios AS u ON m.id_usuario = u.id_usuario
        WHERE m.id_especialidad = :idEspecialidad
    """)
    fun getMedicosByEspecialidad(idEspecialidad: Long): Flow<List<MedicoInfo>>
}