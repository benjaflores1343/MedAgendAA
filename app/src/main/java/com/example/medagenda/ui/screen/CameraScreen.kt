package com.example.medagenda.ui.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.medagenda.BuildConfig
import com.example.medagenda.di.ViewModelFactory
import java.io.File

@Composable
fun CameraScreen(
    patientId: Long,
    onPhotoSaved: () -> Unit
) {
    val context = LocalContext.current
    val cameraViewModel: CameraViewModel = viewModel(factory = ViewModelFactory(context))
    val state by cameraViewModel.state.collectAsState()
    var hasPermission by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasPermission = isGranted
        }
    )

    LaunchedEffect(Unit) {
        when (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)) {
            PackageManager.PERMISSION_GRANTED -> hasPermission = true
            else -> permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(state.saveResult) {
        state.saveResult?.let {
            if (it.isSuccess) {
                Toast.makeText(context, "Receta guardada con éxito", Toast.LENGTH_SHORT).show()
                onPhotoSaved()
            } else {
                Toast.makeText(context, "Error al guardar la receta", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (hasPermission) {
            if (state.photoUri == null) {
                CameraView(onPhotoTaken = { uri -> cameraViewModel.onPhotoTaken(uri) })
            } else {
                PhotoPreview(
                    uri = state.photoUri!!,
                    onRetake = { cameraViewModel.onRetakePhoto() },
                    onSave = { cameraViewModel.saveReceta(patientId) },
                    isSaving = state.isSaving
                )
            }
        } else {
            Text("Se necesita permiso para usar la cámara.", modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun CameraView(onPhotoTaken: (Uri) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val imageCapture = remember { ImageCapture.Builder().build() }

    Box(modifier = Modifier.fillMaxSize()) { // This Box is the fix
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val executor = ContextCompat.getMainExecutor(ctx)

                ProcessCameraProvider.getInstance(ctx).addListener({
                    val cameraProvider = ProcessCameraProvider.getInstance(ctx).get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)
                    } catch (e: Exception) {
                        Log.e("CameraScreen", "Error al vincular la cámara", e)
                    }
                }, executor)

                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        IconButton(
            onClick = { captureImage(imageCapture, context, onPhotoTaken) },
            modifier = Modifier
                .align(Alignment.BottomCenter) // This is now valid
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Camera, contentDescription = "Tomar foto", modifier = Modifier.size(64.dp))
        }
    }
}

private fun captureImage(imageCapture: ImageCapture, context: Context, onPhotoTaken: (Uri) -> Unit) {
    val file = createImageFile(context)
    val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)

    val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                onPhotoTaken(uri)
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraScreen", "Error al guardar la foto", exception)
                Toast.makeText(context, "Error al guardar la foto", Toast.LENGTH_SHORT).show()
            }
        }
    )
}

@Composable
private fun PhotoPreview(uri: Uri, onRetake: () -> Unit, onSave: () -> Unit, isSaving: Boolean) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = rememberAsyncImagePainter(uri),
            contentDescription = "Vista previa de la foto",
            modifier = Modifier.fillMaxSize()
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = onRetake) { Text("Volver a tomar") }
            Button(onClick = onSave, enabled = !isSaving) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Guardar")
                }
            }
        }
    }
}
