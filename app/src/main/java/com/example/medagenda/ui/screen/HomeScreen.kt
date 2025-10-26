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
    userId: Long,
    onLogout: () -> Unit,
    onGoToRequestAppointment: (Long) -> Unit,
    onGoToMyAppointments: (Long) -> Unit,
) {
    val context = LocalContext.current
    val homeScreenVm: HomeScreenVm = viewModel(factory = ViewModelFactory(context))
    val state by homeScreenVm.state.collectAsState()

    LaunchedEffect(key1 = userId) { // Use userId as the key
        if (userId != -1L) {
            homeScreenVm.onEvent(HomeScreenEvent.LoadPatientId(userId))
        }
    }

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

            Spacer(modifier = Modifier.height(32.dp))

            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
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
                } ?: run {
                    // Optional: Show a message if patientId is not found
                    Text("No se pudieron cargar los datos del paciente.")
                }
            }
        }
    }
}