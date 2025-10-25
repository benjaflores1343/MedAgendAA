package com.example.medagenda.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "horarios",
    foreignKeys = [
        ForeignKey(
            entity = Medico::class,
            parentColumns = ["id_medico"],
            childColumns = ["id_medico"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Horario(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_horario")
    val idHorario: Long = 0,

    @ColumnInfo(name = "id_medico", index = true)
    val idMedico: Long,

    @ColumnInfo(name = "fecha_hora_inicio")
    val fechaHoraInicio: Long, // Using Long for Unix timestamp

    @ColumnInfo(name = "fecha_hora_fin")
    val fechaHoraFin: Long,

    // e.g., "Disponible", "Reservado", "Cancelado"
    val estado: String = "Disponible"
)