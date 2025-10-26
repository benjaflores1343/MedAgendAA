package com.example.medagenda.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medagenda.di.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentDetailScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val vm: AppointmentDetailVm = viewModel(factory = ViewModelFactory(context))
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de la Cita") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                state.error != null -> {
                    Text(
                        text = state.error!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.appointment != null -> {
                    val appointment = state.appointment!!
                    val dateFormatter = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("es", "ES"))
                    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        // Appointment Details
                        SectionTitle("Datos de la Cita")
                        DetailRow("Fecha:", dateFormatter.format(appointment.fechaHoraInicio))
                        DetailRow("Hora:", "${timeFormatter.format(appointment.fechaHoraInicio)} - ${timeFormatter.format(appointment.fechaHoraFin)}")
                        DetailRow("Especialidad:", appointment.nombreEspecialidad)
                        DetailRow("Estado:", appointment.estadoCita)
                        Spacer(modifier = Modifier.height(24.dp))

                        // Patient Details
                        SectionTitle("Datos del Paciente")
                        DetailRow("Nombre:", "${appointment.nombrePaciente} ${appointment.apellidoPaciente}")
                        DetailRow("Email:", appointment.emailPaciente)
                        DetailRow("Teléfono:", appointment.telefonoPaciente)
                        Spacer(modifier = Modifier.height(24.dp))

                        // Doctor Details
                        SectionTitle("Datos del Médico")
                        DetailRow("Nombre:", "Dr. ${appointment.nombreMedico} ${appointment.apellidoMedico}")
                        DetailRow("Email:", appointment.emailMedico)
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    Divider()
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = "$label ",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
