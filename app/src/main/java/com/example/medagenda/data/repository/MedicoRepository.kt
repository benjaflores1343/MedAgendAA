package com.example.medagenda.data.repository

import com.example.medagenda.data.local.dao.MedicoDao
import com.example.medagenda.data.local.dto.MedicoInfo
import kotlinx.coroutines.flow.Flow

class MedicoRepository(private val medicoDao: MedicoDao) {

    fun getMedicosByEspecialidad(idEspecialidad: Long): Flow<List<MedicoInfo>> {
        return medicoDao.getMedicosByEspecialidad(idEspecialidad)
    }
}