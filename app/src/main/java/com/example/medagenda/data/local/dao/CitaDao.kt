package com.example.medagenda.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.medagenda.data.local.dto.AppointmentFullDetails
import com.example.medagenda.data.local.dto.AppointmentInfo
import com.example.medagenda.data.local.dto.DoctorAppointmentInfo
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

    @Query("SELECT EXISTS(SELECT 1 FROM citas WHERE id_horario = :horarioId)")
    suspend fun isTimeSlotBooked(horarioId: Long): Boolean

    @Query("UPDATE citas SET estado = :newStatus WHERE id_cita = :citaId")
    suspend fun updateAppointmentStatus(citaId: Long, newStatus: String)

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

    @Query("""
        SELECT
            c.id_cita AS idCita,
            h.fecha_hora_inicio AS fechaHoraInicio,
            p_user.nombre AS nombrePaciente,
            p_user.apellido AS apellidoPaciente,
            c.estado AS estadoCita
        FROM citas AS c
        INNER JOIN horarios AS h ON c.id_horario = h.id_horario
        INNER JOIN pacientes AS p ON c.id_paciente = p.id_paciente
        INNER JOIN usuarios AS p_user ON p.id_usuario = p_user.id_usuario
        WHERE c.id_medico = :medicoId
        ORDER BY h.fecha_hora_inicio ASC
    """)
    fun getAppointmentsForDoctor(medicoId: Long): Flow<List<DoctorAppointmentInfo>>

    @Query("""
        SELECT
            c.id_cita as idCita,
            h.fecha_hora_inicio as fechaHoraInicio,
            h.fecha_hora_fin as fechaHoraFin,
            c.estado as estadoCita,
            
            p_user.nombre as nombrePaciente,
            p_user.apellido as apellidoPaciente,
            p_user.email as emailPaciente,
            p_user.telefono as telefonoPaciente,

            m_user.nombre as nombreMedico,
            m_user.apellido as apellidoMedico,
            m_user.email as emailMedico,

            e.nombre_especialidad as nombreEspecialidad

        FROM citas AS c
        INNER JOIN horarios AS h ON c.id_horario = h.id_horario
        INNER JOIN pacientes AS p ON c.id_paciente = p.id_paciente
        INNER JOIN usuarios AS p_user ON p.id_usuario = p_user.id_usuario
        INNER JOIN medicos AS m ON c.id_medico = m.id_medico
        INNER JOIN usuarios AS m_user ON m.id_usuario = m_user.id_usuario
        INNER JOIN especialidades AS e ON m.id_especialidad = e.id_especialidad
        WHERE c.id_cita = :citaId
    """)
    suspend fun getAppointmentDetails(citaId: Long): AppointmentFullDetails?
}