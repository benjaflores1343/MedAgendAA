package com.example.medagenda.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.medagenda.data.local.dto.UserInfo
import com.example.medagenda.data.local.entity.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUsuario(usuario: Usuario): Long // Returns the new user's ID

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): Usuario?

    @Query("""
        SELECT 
            u.id_usuario AS idUsuario, 
            u.nombre, 
            u.apellido, 
            u.email, 
            r.nombre_rol AS nombreRol
        FROM usuarios AS u
        INNER JOIN usuarios_roles AS ur ON u.id_usuario = ur.id_usuario
        INNER JOIN roles AS r ON ur.id_rol = r.id_rol
        ORDER BY u.apellido ASC
    """)
    fun getAllUsersWithRoles(): Flow<List<UserInfo>>
}