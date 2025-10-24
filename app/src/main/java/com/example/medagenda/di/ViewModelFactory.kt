package com.example.medagenda.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medagenda.data.local.database.MedAgendaDatabaseProvider
import com.example.medagenda.data.repository.UsuarioRepositoryImpl
import com.example.medagenda.domain.repository.UsuarioRepository
import com.example.medagenda.ui.screen.RegisterScreenVm

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val db by lazy { MedAgendaDatabaseProvider.getDatabase(context) }
    private val usuarioRepository: UsuarioRepository by lazy { UsuarioRepositoryImpl(db.usuarioDao()) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterScreenVm::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterScreenVm(usuarioRepository) as T
        }
        // Aquí se pueden añadir otros ViewModels en el futuro
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}