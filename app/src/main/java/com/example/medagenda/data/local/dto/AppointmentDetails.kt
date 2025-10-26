package com.example.medagenda.data.local.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.example.medagenda.data.local.entity.Cita
import com.example.medagenda.data.local.entity.Especialidad
import com.example.medagenda.data.local.entity.Horario
import com.example.medagenda.data.local.entity.Medico
import com.example.medagenda.data.local.entity.Usuario

data class AppointmentDetails(
    @Embedded val cita: Cita,

    @Relation(
        parentColumn = "id_medico",
        entityColumn = "id_medico"
    )
    val medico: Medico,

    @Relation(
        parentColumn = "id_horario",
        entityColumn = "id_horario"
    )
    val horario: Horario,

    // We need to fetch the doctor's user data separately
    // This can be done in the repository or ViewModel
    // For now, we'll leave it as a placeholder.
    @Transient val usuarioMedico: Usuario? = null,
    
    // We'll also fetch the specialty separately.
    @Transient val especialidad: Especialidad? = null
)