package com.example.medagenda.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "roles",
    indices = [Index(value = ["nombre_rol"], unique = true)]
)
data class Rol(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_rol")
    val idRol: Long = 0, // This default value is NECESSARY for autoGenerate to work

    @ColumnInfo(name = "nombre_rol")
    val nombreRol: String,

    val descripcion: String
)
