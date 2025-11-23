package com.example.medagenda.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.medagenda.di.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecetasScreen(
    pacienteId: Long,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val vm: RecetasViewModel = viewModel(factory = ViewModelFactory(context))
    val state by vm.state.collectAsState()

    LaunchedEffect(pacienteId) {
        vm.loadRecetas(pacienteId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Recetas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (state.selectedRecetaIds.isNotEmpty()) {
                        IconButton(onClick = { vm.onEvent(RecetasEvent.DeleteSelected) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${state.error}")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(state.recetas) { receta ->
                    val isSelected = state.selectedRecetaIds.contains(receta.idReceta)
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { vm.onEvent(RecetasEvent.ToggleSelection(receta.idReceta)) }
                    ) {
                        Box {
                            Image(
                                painter = rememberAsyncImagePainter(receta.uriFoto),
                                contentDescription = "Receta",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.aspectRatio(1f)
                            )
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .background(Color.Black.copy(alpha = 0.5f))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Seleccionado",
                                        tint = Color.White,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}