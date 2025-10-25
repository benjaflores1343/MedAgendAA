package com.example.medagenda.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medagenda.di.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String,
    userRole: String,
    onLogout: () -> Unit,
) {
    val context = LocalContext.current
    val homeScreenVm: HomeScreenVm = viewModel(factory = ViewModelFactory(context))

    LaunchedEffect(key1 = Unit) {
        homeScreenVm.logoutResults.collect {
            when (it) {
                is LogoutResult.Success -> {
                    onLogout()
                }
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
                .padding(paddingValues),
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

            // Aquí puedes empezar a mostrar contenido diferente según el rol
            when (userRole) {
                "Paciente" -> {
                    // Composable para la vista de Paciente
                    Text("Aquí verás tus próximas citas.")
                }
                "Médico" -> {
                    // Composable para la vista de Médico
                    Text("Aquí verás tu agenda de hoy.")
                }
                "Administrador" -> {
                    // Composable para la vista de Administrador
                    Text("Aquí tendrás acceso a la gestión de usuarios y reportes.")
                }
            }
        }
    }
}