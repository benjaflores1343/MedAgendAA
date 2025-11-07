package com.example.medagenda.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.medagenda.data.local.dto.MedicoInfo
import com.example.medagenda.di.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDoctorScreen(
    pacienteId: Long,
    specialtyId: Long,
    onDoctorSelected: (Long, Long) -> Unit
) {
    val context = LocalContext.current
    val vm: SelectDoctorViewModel = viewModel(factory = ViewModelFactory(context))
    val state by vm.state.collectAsState()

    LaunchedEffect(specialtyId) {
        vm.loadDoctorsBySpecialty(specialtyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Seleccionar Doctor") })
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                items(state.doctors) { doctor ->
                    DoctorCard(
                        doctor = doctor, 
                        onClick = { onDoctorSelected(doctor.idMedico, pacienteId) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DoctorCard(doctor: MedicoInfo, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${doctor.nombre} ${doctor.apellido}", style = MaterialTheme.typography.titleMedium)
            Text(text = doctor.especialidad, style = MaterialTheme.typography.bodyMedium)
        }
    }
}