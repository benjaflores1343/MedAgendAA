package com.example.medagenda.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuarios",
    indices = [
        Index(value = ["rut"], unique = true),
        Index(value = ["email"], unique = true)
    ]
)
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_usuario")
    val idUsuario: Long = 0,

    val rut: String,
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String,

    @ColumnInfo(name = "contrasena_hash")
    val contrasenaHash: String,

    @ColumnInfo(name = "fecha_registro")
    val fechaRegistro: Long = System.currentTimeMillis(),

    val activo: Boolean = true
)