package com.example.medagenda.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.medagenda.data.local.dto.AppointmentInfo
import com.example.medagenda.data.local.entity.Cita
import kotlinx.coroutines.flow.Flow

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

    @Query("""
        SELECT 
            c.id_cita AS idCita, 
            h.fecha_hora_inicio AS fechaHoraInicio,
            u.nombre AS nombreMedico,
            u.apellido AS apellidoMedico,
            e.nombre_especialidad AS nombreEspecialidad,
            c.estado AS estadoCita
        FROM citas AS c
        INNER JOIN horarios AS h ON c.id_horario = h.id_horario
        INNER JOIN medicos AS m ON c.id_medico = m.id_medico
        INNER JOIN usuarios AS u ON m.id_usuario = u.id_usuario
        INNER JOIN especialidades AS e ON m.id_especialidad = e.id_especialidad
        WHERE c.id_paciente = :patientId
        ORDER BY h.fecha_hora_inicio DESC
    """)
    fun getAppointmentsForPatient(patientId: Long): Flow<List<AppointmentInfo>>
}