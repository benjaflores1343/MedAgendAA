package com.example.medagenda.ui.screen

import android.widget.Toast
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
import com.example.medagenda.data.local.entity.Horario
import com.example.medagenda.di.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        state.bookingResult?.let {
            if (it.isSuccess) {
                Toast.makeText(context, "Cita reservada con éxito", Toast.LENGTH_SHORT).show()
                onBookingConfirmed()
            } else {
                Toast.makeText(context, "Error al reservar la cita", Toast.LENGTH_SHORT).show()
            }
        }
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
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                items(state.timeSlots) { timeSlot ->
                    TimeSlotCard(timeSlot = timeSlot, onClick = {
                        vm.bookAppointment(
                            pacienteId = pacienteId,
                            timeSlot = timeSlot,
                            medicoId = medicoId
                        )
                    })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeSlotCard(timeSlot: Horario, onClick: () -> Unit) {
    val date = Date(timeSlot.fechaHoraInicio)
    val formatter = SimpleDateFormat("EEEE, dd 'de' MMMM, HH:mm", Locale.getDefault())
    val formattedDate = formatter.format(date)

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = formattedDate, style = MaterialTheme.typography.titleMedium)
        }
    }
}