package com.example.medagenda.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.medagenda.data.local.entity.Cita

@Dao
interface CitaDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCita(cita: Cita)

    @Query("UPDATE horarios SET estado = 'Reservado' WHERE id_horario = :idHorario")
    suspend fun markHorarioAsReserved(idHorario: Long)

    @Transaction
    suspend fun createAppointment(cita: Cita) {
        insertCita(cita)
        markHorarioAsReserved(cita.idHorario)
    }
}