package com.example.medagenda.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medagenda.data.local.database.MedAgendaDatabaseProvider
import com.example.medagenda.data.repository.UsuarioRepositoryImpl
import com.example.medagenda.domain.repository.UsuarioRepository
import com.example.medagenda.ui.screen.HomeScreenVm
import com.example.medagenda.ui.screen.LoginScreenVm
import com.example.medagenda.ui.screen.RegisterScreenVm
import com.example.medagenda.ui.screen.RequestAppointmentVm

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val db by lazy { MedAgendaDatabaseProvider.getDatabase(context) }
    private val usuarioRepository: UsuarioRepository by lazy {
        UsuarioRepositoryImpl(db.usuarioDao(), db.rolDao(), db.pacienteDao(), db.especialidadDao(), db.medicoDao())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterScreenVm::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                RegisterScreenVm(usuarioRepository) as T
            }
            modelClass.isAssignableFrom(LoginScreenVm::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                LoginScreenVm(usuarioRepository) as T
            }
            modelClass.isAssignableFrom(HomeScreenVm::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                HomeScreenVm() as T
            }
            modelClass.isAssignableFrom(RequestAppointmentVm::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                RequestAppointmentVm(usuarioRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}