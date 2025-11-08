package com.example.medagenda.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "especialidades",
    indices = [Index(value = ["nombre_especialidad"], unique = true)]
)
data class Especialidad(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_especialidad")
    val idEspecialidad: Long = 0, // This default value is NECESSARY

    @ColumnInfo(name = "nombre_especialidad")
    val nombreEspecialidad: String,

    val descripcion: String
)
