package com.example.medagenda.ui.screen

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medagenda.di.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String,
    userRole: String,
    userId: Long, // Now receives the generic userId
    onLogout: () -> Unit,
    onGoToRequestAppointment: (Long) -> Unit,
    onGoToMyAppointments: (Long) -> Unit,
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
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
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
            verticalArrangement = Arrangement.Center,
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
                    } ?: CircularProgressIndicator() // Show a loader while fetching pacienteId
                }
                "Médico" -> {
                    Text("Aquí verás tu agenda de hoy.")
                }
                "Administrador" -> {
                    Text("Aquí tendrás acceso a la gestión de usuarios y reportes.")
                }
            }
        }
    }
}