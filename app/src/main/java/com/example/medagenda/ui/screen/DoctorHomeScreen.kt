package com.example.medagenda.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medagenda.data.local.dto.DoctorAppointmentInfo
import com.example.medagenda.di.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorHomeScreen(
    medicoId: Long,
    userName: String,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val vm: DoctorHomeViewModel = viewModel(factory = ViewModelFactory(context))
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Citas - Dr. $userName") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Cerrar sesión")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            state.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.error!!)
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    items(state.appointments) { appointment ->
                        DoctorAppointmentCard(appointment = appointment)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DoctorAppointmentCard(appointment: DoctorAppointmentInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Paciente: ${appointment.nombrePaciente} ${appointment.apellidoPaciente}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Fecha: ${appointment.fechaHoraInicio}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Estado: ${appointment.estadoCita}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}