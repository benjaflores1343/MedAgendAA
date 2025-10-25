package com.example.medagenda.ui.screen

import android.widget.Toast
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medagenda.data.local.entity.Horario
import com.example.medagenda.di.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTimeSlotScreen(
    medicoId: Long, 
    pacienteId: Long,
    onBookingConfirmed: () -> Unit
) {
    val context = LocalContext.current
    val vm: SelectTimeSlotVm = viewModel(factory = ViewModelFactory(context))
    val horarios by vm.horariosState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        vm.bookingResult.collect {
            when (it) {
                is BookingResult.Success -> {
                    Toast.makeText(context, "¡Cita reservada con éxito!", Toast.LENGTH_LONG).show()
                    onBookingConfirmed()
                }
                is BookingResult.Error -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Seleccionar Horario") })
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
                    text = "Horarios disponibles",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            items(horarios) { horario ->
                TimeSlotCard(
                    horario = horario,
                    onClick = { vm.bookAppointment(pacienteId, horario) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeSlotCard(horario: Horario, onClick: () -> Unit) {
    val formatter = SimpleDateFormat("EEEE, dd MMMM yyyy, HH:mm", Locale("es", "ES"))
    val startTime = formatter.format(horario.fechaHoraInicio)

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = startTime,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}