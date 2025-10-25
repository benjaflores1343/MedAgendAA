package com.example.medagenda.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuario_rol",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id_usuario"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Rol::class,
            parentColumns = ["id_rol"],
            childColumns = ["id_rol"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UsuarioRol(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_usuario_rol")
    val idUsuarioRol: Long = 0,

    @ColumnInfo(name = "id_usuario", index = true)
    val idUsuario: Long,

    @ColumnInfo(name = "id_rol", index = true)
    val idRol: Long,

    @ColumnInfo(name = "fecha_asignacion")
    val fechaAsignacion: Long = System.currentTimeMillis()
)