package com.example.medagenda.data.repository

import com.example.medagenda.data.local.dao.UsuarioDao
import com.example.medagenda.data.local.entity.Usuario
import com.example.medagenda.domain.repository.UsuarioRepository

class UsuarioRepositoryImpl(
    private val usuarioDao: UsuarioDao
) : UsuarioRepository {

    override suspend fun registerUser(usuario: Usuario) {
        usuarioDao.insertUsuario(usuario)
    }
}