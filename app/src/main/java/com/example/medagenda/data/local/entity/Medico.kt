package com.example.medagenda.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "medicos",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id_usuario"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Especialidad::class,
            parentColumns = ["id_especialidad"],
            childColumns = ["id_especialidad"],
            onDelete = ForeignKey.SET_NULL // If an specialty is deleted, don't delete the doctor
        )
    ]
)
data class Medico(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_medico")
    val idMedico: Long = 0, // Restored the default value

    @ColumnInfo(name = "id_usuario", index = true)
    val idUsuario: Long,

    @ColumnInfo(name = "id_especialidad", index = true)
    val idEspecialidad: Long?,

    val biografia: String? = null,

    @ColumnInfo(name = "horario_consulta")
    val horarioConsulta: String? = null // e.g., "Lunes a Viernes, 9am - 5pm"
)
