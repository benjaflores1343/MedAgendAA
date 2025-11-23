package com.example.medagenda.ui.screen

import androidx.compose.foundation.clickable
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
import com.example.medagenda.data.network.HorarioApi
import com.example.medagenda.di.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTimeSlotScreen(
    medicoId: Long,
    pacienteId: Long,
    onBookingConfirmed: () -> Unit
) {
    val context = LocalContext.current
    val vm: SelectTimeSlotViewModel = viewModel(factory = ViewModelFactory(context))
    val state by vm.state.collectAsState()

    LaunchedEffect(medicoId) {
        vm.loadAvailableTimeSlots(medicoId)
    }

    LaunchedEffect(state.bookingResult) {
        state.bookingResult?.onSuccess { onBookingConfirmed() }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Seleccionar Horario") })
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
                items(state.timeSlots) { timeSlot ->
                    TimeSlotCard(timeSlot = timeSlot, onClick = {
                        vm.bookAppointment(pacienteId, timeSlot, medicoId)
                    })
                }
            }
        }
    }
}

@Composable
private fun TimeSlotCard(timeSlot: HorarioApi, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "De: ${timeSlot.fechaHoraInicio}", style = MaterialTheme.typography.titleMedium)
            Text(text = "A: ${timeSlot.fechaHoraFin}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}