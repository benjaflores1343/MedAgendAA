package com.example.medagenda.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medagenda.data.network.AppointmentApiResponse
import com.example.medagenda.di.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppointmentsScreen(
    onGoToAppointmentDetail: (Long) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val vm: MyAppointmentsVm = viewModel(factory = ViewModelFactory(context))
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Citas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${state.error}")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                items(state.appointments) { appointment ->
                    AppointmentCard(appointment = appointment, onClick = {
                        onGoToAppointmentDetail(appointment.idCita)
                    })
                }
            }
        }
    }
}

@Composable
private fun AppointmentCard(appointment: AppointmentApiResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Dr. ${appointment.nombreMedico} ${appointment.apellidoMedico}", style = MaterialTheme.typography.titleMedium)
            Text(text = appointment.nombreEspecialidad, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Fecha: ${appointment.fechaHoraInicio}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Estado: ${appointment.estadoCita}", style = MaterialTheme.typography.bodySmall)
        }
    }
}