package com.example.medagenda.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
    onGoToCamera: (Long) -> Unit,
    onGoToMyRecipes: (Long) -> Unit, // Added for recipe gallery
) {
    val context = LocalContext.current
    val homeScreenVm: HomeScreenVm = viewModel(factory = ViewModelFactory(context))
    val state by homeScreenVm.state.collectAsState()

    LaunchedEffect(key1 = userId) {
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
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "¿Qué necesitas hacer hoy?",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                state.pacienteId?.let { patientId ->
                    DashboardMenu(
                        onGoToRequestAppointment = { onGoToRequestAppointment(patientId) },
                        onGoToMyAppointments = { onGoToMyAppointments(patientId) },
                        onGoToCamera = { onGoToCamera(patientId) },
                        onGoToMyRecipes = { onGoToMyRecipes(patientId) } // Pass patientId
                    )
                } ?: run {
                    Text("No se pudieron cargar los datos del paciente.")
                }
            }
        }
    }
}

@Composable
private fun DashboardMenu(
    onGoToRequestAppointment: () -> Unit,
    onGoToMyAppointments: () -> Unit,
    onGoToCamera: () -> Unit,
    onGoToMyRecipes: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        DashboardCard(
            title = "Solicitar una Cita",
            icon = Icons.Default.AddCircle,
            onClick = onGoToRequestAppointment
        )
        DashboardCard(
            title = "Mis Citas",
            icon = Icons.Default.DateRange,
            onClick = onGoToMyAppointments
        )
        DashboardCard(
            title = "Mis Recetas",
            icon = Icons.Default.PhotoLibrary,
            onClick = onGoToMyRecipes
        )
        DashboardCard(
            title = "Tomar foto receta",
            icon = Icons.Default.PhotoCamera,
            onClick = onGoToCamera
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardCard(title: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, style = MaterialTheme.typography.titleLarge)
        }
    }
}