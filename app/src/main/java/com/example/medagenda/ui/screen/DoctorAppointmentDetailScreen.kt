package com.example.medagenda.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medagenda.di.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorAppointmentDetailScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val vm: DoctorAppointmentDetailViewModel = viewModel(factory = ViewModelFactory(context))
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de la Cita") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            // Show loading indicator
        } else if (state.appointment != null) {
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
                Text(text = "Paciente: ${state.appointment!!.nombrePaciente} ${state.appointment!!.apellidoPaciente}", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Email: ${state.appointment!!.emailPaciente}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Teléfono: ${state.appointment!!.telefonoPaciente}", style = MaterialTheme.typography.bodyLarge)
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                Text(text = "Fecha: ${state.appointment!!.fechaHoraInicio}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Especialidad: ${state.appointment!!.nombreEspecialidad}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Estado: ${state.appointment!!.estadoCita}", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            // Show error message
        }
    }
}
