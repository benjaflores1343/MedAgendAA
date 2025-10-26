package com.example.medagenda.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String,
    userRole: String,
    userId: Long,
    onLogout: () -> Unit,
    onGoToRequestAppointment: (Long) -> Unit,
    onGoToMyAppointments: (Long) -> Unit,
    onGoToAppointmentDetail: (Long) -> Unit,
    onGoToUserList: () -> Unit, // New navigation event
) {
    val context = LocalContext.current
    val homeScreenVm: HomeScreenVm = viewModel(factory = ViewModelFactory(context))
    val state by homeScreenVm.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        homeScreenVm.logoutResults.collect {
            when (it) {
                is LogoutResult.Success -> onLogout()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MedAgenda") },
                actions = {
                    IconButton(onClick = { homeScreenVm.onEvent(HomeScreenEvent.LogoutClicked) }) {
                        Icon(Icons.Default.Logout, contentDescription = "Cerrar sesión")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¡Bienvenido, $userName!",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Rol: $userRole",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                when (userRole) {
                    "Paciente" -> {
                        state.pacienteId?.let {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Button(onClick = { onGoToRequestAppointment(it) }) {
                                    Text("Solicitar una Cita")
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = { onGoToMyAppointments(it) }) {
                                    Text("Mis Citas")
                                }
                            }
                        }
                    }
                    "Médico" -> {
                        if (state.doctorAppointments.isEmpty()) {
                            Text("No tienes citas programadas para hoy.")
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                item {
                                    Text(
                                        text = "Tus citas de hoy",
                                        style = MaterialTheme.typography.headlineSmall,
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    )
                                }
                                items(state.doctorAppointments) {
                                    DoctorAppointmentCard(
                                        appointment = it,
                                        onCardClicked = { onGoToAppointmentDetail(it.idCita) }
                                    )
                                }
                            }
                        }
                    }
                    "Administrador" -> {
                        Button(onClick = onGoToUserList) {
                            Text("Gestionar Usuarios")
                        }
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
    onCardClicked: () -> Unit
) {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    val startTime = formatter.format(appointment.fechaHoraInicio)

    Card(
        onClick = onCardClicked, // Make the card clickable
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${appointment.nombrePaciente} ${appointment.apellidoPaciente}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Hora: $startTime",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = appointment.estadoCita,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.End
            )
        }
    }
}