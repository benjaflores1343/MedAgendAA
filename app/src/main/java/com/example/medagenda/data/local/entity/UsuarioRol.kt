package com.example.medagenda.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "usuarios_roles", // Explicit table name
    primaryKeys = ["idUsuario", "idRol"],
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["idUsuario"],
            childColumns = ["idUsuario"],
            onDelete = ForeignKey.CASCADE // Optional: what to do if a user is deleted
        ),
        ForeignKey(
            entity = Rol::class,
            parentColumns = ["idRol"],
            childColumns = ["idRol"],
            onDelete = ForeignKey.CASCADE // Optional: what to do if a role is deleted
        )
    ]
)
data class UsuarioRol(
    val idUsuario: Long,
    val idRol: Long
)
