package com.example.medagenda.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "usuarios_roles", // Be explicit with the table name
    primaryKeys = ["id_usuario", "id_rol"], // Use database column names
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id_usuario"],      // Correct: Use database column name from Usuario table
            childColumns = ["id_usuario"],       // Correct: Use database column name from this table
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Rol::class,
            parentColumns = ["id_rol"],          // Correct: Use database column name from Rol table
            childColumns = ["id_rol"],           // Correct: Use database column name from this table
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["id_usuario"]), Index(value = ["id_rol"])] // Add indices to satisfy KSP warning
)
data class UsuarioRol(
    @ColumnInfo(name = "id_usuario") // Explicitly map property to column
    val idUsuario: Long,

    @ColumnInfo(name = "id_rol")     // Explicitly map property to column
    val idRol: Long
)
