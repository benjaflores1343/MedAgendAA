package com.example.medagenda.ui.screen

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medagenda.data.local.dto.DoctorAppointmentInfo
import com.example.medagenda.di.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorHomeScreen(
    medicoId: Long,
    userName: String,
    onLogout: () -> Unit,
    onAppointmentClick: (Long) -> Unit // Added for navigation to detail screen
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
                    Text(text = state.error!!, textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))
                }
            }
            state.appointments.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No tienes citas programadas.", style = MaterialTheme.typography.bodyLarge)
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
                        DoctorAppointmentCard(
                            appointment = appointment,
                            onApprove = { vm.onEvent(DoctorHomeEvent.ApproveAppointment(appointment.idCita)) },
                            onReject = { vm.onEvent(DoctorHomeEvent.RejectAppointment(appointment.idCita)) },
                            onClick = { onAppointmentClick(appointment.idCita) } // Pass click event
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DoctorAppointmentCard(
    appointment: DoctorAppointmentInfo,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onClick: () -> Unit
) {
    OutlinedCard(
        onClick = onClick, // Make the card clickable
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${appointment.nombrePaciente} ${appointment.apellidoPaciente}", style = MaterialTheme.typography.titleLarge)
            Text(text = "Fecha: ${appointment.fechaHoraInicio}", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 8.dp))
            Text(text = "Estado: ${appointment.estadoCita}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 4.dp))

            if (appointment.estadoCita == "Programada") {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = onApprove) {
                        Text("Aprobar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(onClick = onReject) {
                        Text("Rechazar")
                    }
                }
            }
        }
    }
}