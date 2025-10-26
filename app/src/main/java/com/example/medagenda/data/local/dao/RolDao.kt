package com.example.medagenda.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.medagenda.data.local.entity.Rol
import com.example.medagenda.data.local.entity.UsuarioRol

@Dao
interface RolDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRoles(roles: List<Rol>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun assignRolToUser(usuarioRol: UsuarioRol)

    @Query("SELECT * FROM roles WHERE nombre_rol = :nombreRol LIMIT 1")
    suspend fun findRolByName(nombreRol: String): Rol?

    @Query("""
        SELECT r.* FROM roles r
        INNER JOIN usuarios_roles ur ON r.id_rol = ur.id_rol
        WHERE ur.id_usuario = :idUsuario
        LIMIT 1
    """)
    suspend fun getRolForUser(idUsuario: Long): Rol?
}