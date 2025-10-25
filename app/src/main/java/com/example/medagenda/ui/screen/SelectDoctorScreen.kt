package com.example.medagenda.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medagenda.data.local.dto.MedicoInfo
import com.example.medagenda.di.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDoctorScreen(
    pacienteId: Long,
    onDoctorSelected: (Long, Long) -> Unit
) {
    val context = LocalContext.current
    val vm: SelectDoctorVm = viewModel(factory = ViewModelFactory(context))
    val medicos by vm.medicosState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Seleccionar Médico") })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Médicos disponibles",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            items(medicos) { medico ->
                DoctorCard(
                    medico = medico, 
                    onClick = { onDoctorSelected(medico.idMedico, pacienteId) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DoctorCard(medico: MedicoInfo, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${medico.nombre} ${medico.apellido}",
                style = MaterialTheme.typography.titleMedium
            )
            if (medico.biografia != null) {
                Text(
                    text = medico.biografia,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}