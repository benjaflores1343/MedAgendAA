package com.example.medagenda.ui.screen

import androidx.compose.foundation.clickable
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
import com.example.medagenda.data.local.dto.AppointmentInfo
import com.example.medagenda.di.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppointmentsScreen(
    onGoToAppointmentDetail: (Long) -> Unit, // New navigation event
) {
    val context = LocalContext.current
    val vm: MyAppointmentsVm = viewModel(factory = ViewModelFactory(context))
    val appointments by vm.appointmentsState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mis Citas") })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(appointments) { appointment ->
                AppointmentCard(
                    appointment = appointment,
                    onCardClicked = { onGoToAppointmentDetail(appointment.idCita) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppointmentCard(
    appointment: AppointmentInfo,
    onCardClicked: () -> Unit,
) {
    val formatter = SimpleDateFormat("EEEE, dd MMMM yyyy, HH:mm", Locale("es", "ES"))
    val startTime = formatter.format(appointment.fechaHoraInicio)

    Card(
        onClick = onCardClicked, // Make the card clickable
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = startTime,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Dr. ${appointment.nombreMedico} ${appointment.apellidoMedico}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = appointment.nombreEspecialidad,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Estado: ${appointment.estadoCita}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
