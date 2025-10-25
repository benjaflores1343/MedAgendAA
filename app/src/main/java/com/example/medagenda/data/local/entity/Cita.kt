package com.example.medagenda.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "citas",
    foreignKeys = [
        ForeignKey(
            entity = Paciente::class,
            parentColumns = ["id_paciente"],
            childColumns = ["id_paciente"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Medico::class,
            parentColumns = ["id_medico"],
            childColumns = ["id_medico"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Horario::class,
            parentColumns = ["id_horario"],
            childColumns = ["id_horario"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Cita(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_cita")
    val idCita: Long = 0,

    @ColumnInfo(name = "id_paciente", index = true)
    val idPaciente: Long,

    @ColumnInfo(name = "id_medico", index = true)
    val idMedico: Long,

    @ColumnInfo(name = "id_horario", index = true)
    val idHorario: Long,

    @ColumnInfo(name = "fecha_creacion")
    val fechaCreacion: Long = System.currentTimeMillis(),

    // e.g., "Confirmada", "Cancelada", "Completada"
    val estado: String = "Confirmada",

    val notas: String? = null // Optional notes from the patient
)