package com.example.medagenda.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.medagenda.data.local.entity.Receta
import com.example.medagenda.di.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecetasScreen(pacienteId: Long, onBack: () -> Unit) {
    val context = LocalContext.current
    val vm: RecetasViewModel = viewModel(factory = ViewModelFactory(context))
    val state by vm.state.collectAsState()

    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var selectedImageForDialog by remember { mutableStateOf<Receta?>(null) }

    val isInSelectionMode = state.selectedRecetaIds.isNotEmpty()

    LaunchedEffect(pacienteId) {
        vm.loadRecetas(pacienteId)
    }

    Scaffold(
        topBar = {
            if (isInSelectionMode) {
                SelectionTopAppBar(
                    selectedCount = state.selectedRecetaIds.size,
                    onClearSelection = { vm.onEvent(RecetasEvent.ClearSelection) },
                    onDelete = { showDeleteConfirmation = true }
                )
            } else {
                TopAppBar(
                    title = { Text("Mis Recetas") },
                    navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") } }
                )
            }
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else if (state.recetas.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("No tienes recetas guardadas.") }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(state.recetas, key = { it.idReceta }) { receta ->
                    RecetaItem(
                        receta = receta,
                        isSelected = receta.idReceta in state.selectedRecetaIds,
                        onClick = {
                            if (isInSelectionMode) {
                                vm.onEvent(RecetasEvent.ToggleSelection(receta.idReceta))
                            } else {
                                selectedImageForDialog = receta
                            }
                        },
                        onLongClick = { vm.onEvent(RecetasEvent.ToggleSelection(receta.idReceta)) }
                    )
                }
            }
        }
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Confirmar borrado") },
            text = { Text("¿Estás seguro de que quieres borrar las ${state.selectedRecetaIds.size} recetas seleccionadas?") },
            confirmButton = {
                Button(onClick = {
                    vm.onEvent(RecetasEvent.DeleteSelected)
                    showDeleteConfirmation = false
                }) { Text("Borrar") }
            },
            dismissButton = { Button(onClick = { showDeleteConfirmation = false }) { Text("Cancelar") } }
        )
    }

    selectedImageForDialog?.let {
        ZoomableImageDialog(receta = it, onDismiss = { selectedImageForDialog = null })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RecetaItem(
    receta: Receta,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .then(
                if (isSelected) Modifier.border(4.dp, MaterialTheme.colorScheme.primary) else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(receta.uriFoto),
            contentDescription = "Receta",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Seleccionado",
                tint = Color.White,
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectionTopAppBar(
    selectedCount: Int,
    onClearSelection: () -> Unit,
    onDelete: () -> Unit
) {
    TopAppBar(
        title = { Text("$selectedCount seleccionada(s)") },
        navigationIcon = {
            IconButton(onClick = onClearSelection) { Icon(Icons.Default.Close, "Cerrar selección") }
        },
        actions = {
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "Borrar") }
        }
    )
}

@Composable
fun ZoomableImageDialog(receta: Receta, onDismiss: () -> Unit) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onDoubleTap = {
                        scale = 1f
                        offsetX = 0f
                        offsetY = 0f
                    })
                }
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale *= zoom
                        scale = scale.coerceIn(1f, 3f) // Clamp scale
                        offsetX += pan.x
                        offsetY += pan.y
                    }
                }
        ) {
            Image(
                painter = rememberAsyncImagePainter(receta.uriFoto),
                contentDescription = "Vista completa",
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY
                    ),
                contentScale = ContentScale.Fit
            )
        }
    }
}