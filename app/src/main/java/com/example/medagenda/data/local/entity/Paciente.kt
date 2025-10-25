package com.example.medagenda.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "pacientes",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id_usuario"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Paciente(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_paciente")
    val idPaciente: Long = 0,

    @ColumnInfo(name = "id_usuario", index = true)
    val idUsuario: Long,

    @ColumnInfo(name = "fecha_nacimiento")
    val fechaNacimiento: String? = null, // Can be null for now

    val direccion: String? = null, // Can be null for now
    val alergias: String? = null, // Can be null for now

    @ColumnInfo(name = "condiciones_medicas")
    val condicionesMedicas: String? = null // Can be null for now
)