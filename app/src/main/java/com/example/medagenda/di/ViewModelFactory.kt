package com.example.medagenda.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.medagenda.data.local.database.MedAgendaDatabaseProvider
import com.example.medagenda.data.repository.UsuarioRepositoryImpl
import com.example.medagenda.domain.repository.UsuarioRepository
import com.example.medagenda.ui.screen.*

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val db by lazy { MedAgendaDatabaseProvider.getDatabase(context) }
    private val usuarioRepository: UsuarioRepository by lazy {
        UsuarioRepositoryImpl(db.usuarioDao(), db.rolDao(), db.pacienteDao(), db.especialidadDao(), db.medicoDao(), db.horarioDao(), db.citaDao())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val savedStateHandle = extras.createSavedStateHandle()
        
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
                HomeScreenVm(usuarioRepository, savedStateHandle) as T
            }
            modelClass.isAssignableFrom(RequestAppointmentVm::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                RequestAppointmentVm(usuarioRepository) as T
            }
            modelClass.isAssignableFrom(SelectDoctorVm::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                SelectDoctorVm(usuarioRepository, savedStateHandle) as T
            }
            modelClass.isAssignableFrom(SelectTimeSlotVm::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                SelectTimeSlotVm(usuarioRepository, savedStateHandle) as T
            }
            modelClass.isAssignableFrom(MyAppointmentsVm::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                MyAppointmentsVm(usuarioRepository, savedStateHandle) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}