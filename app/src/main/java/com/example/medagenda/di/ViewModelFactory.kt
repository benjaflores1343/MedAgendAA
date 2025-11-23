package com.example.medagenda.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.medagenda.data.local.database.MedAgendaDatabase
import com.example.medagenda.data.repository.UsuarioRepositoryImpl
import com.example.medagenda.domain.repository.UsuarioRepository
import com.example.medagenda.ui.screen.*

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val db = MedAgendaDatabase.getDatabase(context)
        val usuarioRepository: UsuarioRepository = UsuarioRepositoryImpl(
            db.usuarioDao(),
            db.rolDao(),
            db.pacienteDao(),
            db.especialidadDao(),
            db.medicoDao(),
            db.horarioDao(),
            db.citaDao(),
            db.recetaDao()
        )
        val savedStateHandle = extras.createSavedStateHandle()

        return when {
            modelClass.isAssignableFrom(LoginScreenVm::class.java) -> {
                LoginScreenVm(usuarioRepository) as T
            }
            modelClass.isAssignableFrom(RegisterScreenVm::class.java) -> {
                RegisterScreenVm(usuarioRepository) as T
            }
            modelClass.isAssignableFrom(RequestAppointmentVm::class.java) -> {
                RequestAppointmentVm(usuarioRepository) as T
            }
            modelClass.isAssignableFrom(SelectDoctorViewModel::class.java) -> {
                SelectDoctorViewModel(usuarioRepository) as T
            }
            modelClass.isAssignableFrom(SelectTimeSlotViewModel::class.java) -> {
                SelectTimeSlotViewModel(usuarioRepository) as T
            }
            modelClass.isAssignableFrom(MyAppointmentsVm::class.java) -> {
                MyAppointmentsVm(usuarioRepository, savedStateHandle) as T
            }
            modelClass.isAssignableFrom(CameraViewModel::class.java) -> {
                CameraViewModel(usuarioRepository) as T
            }
            modelClass.isAssignableFrom(RecetasViewModel::class.java) -> {
                RecetasViewModel(usuarioRepository) as T
            }
            modelClass.isAssignableFrom(DoctorHomeViewModel::class.java) -> {
                DoctorHomeViewModel(usuarioRepository, savedStateHandle) as T
            }
            modelClass.isAssignableFrom(HomeScreenVm::class.java) -> {
                HomeScreenVm(usuarioRepository) as T
            }
            modelClass.isAssignableFrom(DoctorAppointmentDetailViewModel::class.java) -> {
                DoctorAppointmentDetailViewModel(usuarioRepository, savedStateHandle) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}