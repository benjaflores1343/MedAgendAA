package com.example.medagenda.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "recetas",
    foreignKeys = [
        ForeignKey(
            entity = Paciente::class,
            parentColumns = ["id_paciente"],
            childColumns = ["id_paciente"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Receta(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_receta")
    val idReceta: Long = 0,

    @ColumnInfo(name = "id_paciente", index = true)
    val idPaciente: Long,

    @ColumnInfo(name = "uri_foto")
    val uriFoto: String,

    @ColumnInfo(name = "fecha_creacion")
    val fechaCreacion: Long = System.currentTimeMillis()
)