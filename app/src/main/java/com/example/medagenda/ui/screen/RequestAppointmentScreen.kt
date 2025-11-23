package com.example.medagenda.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medagenda.data.network.EspecialidadApi
import com.example.medagenda.di.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestAppointmentScreen(
    pacienteId: Long,
    onSpecialtySelected: (Long, Long) -> Unit
) {
    val context = LocalContext.current
    val vm: RequestAppointmentVm = viewModel(factory = ViewModelFactory(context))
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Solicitar Cita - Elige Especialidad") })
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
                items(state.especialidades) { especialidad ->
                    SpecialtyCard(especialidad = especialidad, onClick = {
                        onSpecialtySelected(especialidad.idEspecialidad, pacienteId)
                    })
                }
            }
        }
    }
}

@Composable
private fun SpecialtyCard(especialidad: EspecialidadApi, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = especialidad.nombreEspecialidad, style = MaterialTheme.typography.titleMedium)
            Text(text = especialidad.descripcion, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
