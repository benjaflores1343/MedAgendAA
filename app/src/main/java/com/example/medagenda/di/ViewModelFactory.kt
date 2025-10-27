package com.example.medagenda.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.medagenda.data.local.database.MedAgendaDatabaseProvider
import com.example.medagenda.data.repository.MedicoRepository
import com.example.medagenda.data.repository.UsuarioRepositoryImpl
import com.example.medagenda.domain.repository.UsuarioRepository
import com.example.medagenda.ui.screen.*

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val db by lazy { MedAgendaDatabaseProvider.getDatabase(context) }
    private val usuarioRepository: UsuarioRepository by lazy {
        UsuarioRepositoryImpl(db.usuarioDao(), db.rolDao(), db.pacienteDao(), db.especialidadDao(), db.medicoDao(), db.horarioDao(), db.citaDao())
    }
    private val medicoRepository: MedicoRepository by lazy {
        MedicoRepository(db.medicoDao())
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
                HomeScreenVm(usuarioRepository) as T // Removed savedStateHandle
            }
            modelClass.isAssignableFrom(RequestAppointmentVm::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                RequestAppointmentVm(usuarioRepository) as T
            }
            modelClass.isAssignableFrom(SelectDoctorViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                SelectDoctorViewModel(medicoRepository) as T
            }
            modelClass.isAssignableFrom(SelectTimeSlotViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                SelectTimeSlotViewModel(usuarioRepository) as T
            }
            modelClass.isAssignableFrom(MyAppointmentsVm::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                MyAppointmentsVm(usuarioRepository, savedStateHandle) as T
            }
            modelClass.isAssignableFrom(AppointmentDetailVm::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                AppointmentDetailVm(usuarioRepository, savedStateHandle) as T
            }
            modelClass.isAssignableFrom(UserListVm::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                UserListVm(usuarioRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}