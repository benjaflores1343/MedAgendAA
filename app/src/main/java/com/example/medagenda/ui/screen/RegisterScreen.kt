package com.example.medagenda.ui.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.medagenda.BuildConfig
import com.example.medagenda.di.ViewModelFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = context.externalCacheDir
    return File.createTempFile(imageFileName, ".jpg", storageDir)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterOkNavigateToLogin: () -> Unit,
    onGoLogin: () -> Unit,
) {
    val context = LocalContext.current
    val registerScreenVm: RegisterScreenVm = viewModel(factory = ViewModelFactory(context))
    val state = registerScreenVm.state
    var showDatePicker by remember { mutableStateOf(false) }
    var showImageOptions by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var repeatedPasswordVisible by remember { mutableStateOf(false) }

    // --- Image Logic ---
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                tempImageUri?.let { registerScreenVm.onEvent(RegistrationFormEvent.ProfileImageChanged(it)) }
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                val file = createImageFile(context)
                val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
                tempImageUri = uri
                cameraLauncher.launch(uri)
            }
        }
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let { registerScreenVm.onEvent(RegistrationFormEvent.ProfileImageChanged(it)) }
        }
    )

    fun handleCameraClick() {
        when (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)) {
            PackageManager.PERMISSION_GRANTED -> {
                val file = createImageFile(context)
                val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
                tempImageUri = uri
                cameraLauncher.launch(uri)
            }
            else -> {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        registerScreenVm.validationEvents.collect {
            when (it) {
                is ValidationEvent.Success -> onRegisterOkNavigateToLogin()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "MedAgenda",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Crea tu cuenta en MedAgenda",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = rememberAsyncImagePainter(state.profileImageUri ?: "https://via.placeholder.com/150"),
            contentDescription = "Avatar",
            modifier = Modifier.size(150.dp).clip(CircleShape).clickable { showImageOptions = true },
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- All TextFields and other UI elements... ---
        OutlinedTextField(value = state.nombre, onValueChange = { registerScreenVm.onEvent(RegistrationFormEvent.NombreChanged(it)) }, modifier = Modifier.fillMaxWidth(), label = { Text("Nombre") }, leadingIcon = { Icon(Icons.Default.Person, "Nombre") }, isError = state.nombreError != null)
        AnimatedVisibility(
            visible = state.nombreError != null,
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut()
        ) {
            state.nombreError?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = state.apellido, onValueChange = { registerScreenVm.onEvent(RegistrationFormEvent.ApellidoChanged(it)) }, modifier = Modifier.fillMaxWidth(), label = { Text("Apellido") }, leadingIcon = { Icon(Icons.Default.Person, "Apellido") }, isError = state.apellidoError != null)
        AnimatedVisibility(
            visible = state.apellidoError != null,
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut()
        ) {
            state.apellidoError?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = state.rut, onValueChange = { registerScreenVm.onEvent(RegistrationFormEvent.RutChanged(it)) }, modifier = Modifier.fillMaxWidth(), label = { Text("RUT") }, leadingIcon = { Icon(Icons.Default.Badge, "RUT") }, isError = state.rutError != null)
        AnimatedVisibility(
            visible = state.rutError != null,
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut()
        ) {
            state.rutError?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = state.telefono, onValueChange = { registerScreenVm.onEvent(RegistrationFormEvent.TelefonoChanged(it)) }, modifier = Modifier.fillMaxWidth(), label = { Text("Teléfono") }, leadingIcon = { Icon(Icons.Default.Phone, "Teléfono") }, isError = state.telefonoError != null, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
        AnimatedVisibility(
            visible = state.telefonoError != null,
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut()
        ) {
            state.telefonoError?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth().height(56.dp).border(1.dp, if (state.fechaNacimientoError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline, MaterialTheme.shapes.extraSmall).clickable { showDatePicker = true }.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Cake, contentDescription = "Fecha de Nacimiento", tint = if (state.fechaNacimientoError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.width(16.dp))
            Text(text = if (state.fechaNacimiento.isNotBlank()) state.fechaNacimiento else "Fecha de Nacimiento", color = if (state.fechaNacimiento.isNotBlank()) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant)
        }
        AnimatedVisibility(
            visible = state.fechaNacimientoError != null,
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut()
        ) {
            state.fechaNacimientoError?.let { Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 16.dp, top = 4.dp)) }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = state.direccion, onValueChange = { registerScreenVm.onEvent(RegistrationFormEvent.DireccionChanged(it)) }, modifier = Modifier.fillMaxWidth(), label = { Text("Dirección") }, leadingIcon = { Icon(Icons.Default.Home, "Dirección") }, isError = state.direccionError != null)
        AnimatedVisibility(
            visible = state.direccionError != null,
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut()
        ) {
            state.direccionError?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = state.email, onValueChange = { registerScreenVm.onEvent(RegistrationFormEvent.EmailChanged(it)) }, modifier = Modifier.fillMaxWidth(), label = { Text("Email") }, leadingIcon = { Icon(Icons.Default.Email, "Email") }, isError = state.emailError != null, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
        AnimatedVisibility(
            visible = state.emailError != null,
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut()
        ) {
            state.emailError?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = { registerScreenVm.onEvent(RegistrationFormEvent.PasswordChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, "Password") },
            isError = state.passwordError != null,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, "Toggle password visibility")
                }
            }
        )
        AnimatedVisibility(
            visible = state.passwordError != null,
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut()
        ) {
            state.passwordError?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.repeatedPassword,
            onValueChange = { registerScreenVm.onEvent(RegistrationFormEvent.RepeatedPasswordChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Repetir contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, "Password") },
            isError = state.repeatedPasswordError != null,
            visualTransformation = if (repeatedPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (repeatedPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { repeatedPasswordVisible = !repeatedPasswordVisible }) {
                    Icon(imageVector = image, "Toggle password visibility")
                }
            }
        )
        AnimatedVisibility(
            visible = state.repeatedPasswordError != null,
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut()
        ) {
            state.repeatedPasswordError?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = state.acceptedTerms, onCheckedChange = { registerScreenVm.onEvent(RegistrationFormEvent.AcceptTerms(it)) })
            Text("Acepto los términos y condiciones")
        }
        AnimatedVisibility(visible = state.termsError != null) {
            state.termsError?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
        }

        // Display general registration error
        AnimatedVisibility(visible = state.registrationError != null) {
            state.registrationError?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { registerScreenVm.onEvent(RegistrationFormEvent.Submit) }, modifier = Modifier.fillMaxWidth()) { Text("Registrarse") }
        Spacer(modifier = Modifier.height(8.dp))

        val annotatedString = buildAnnotatedString { withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) { append("¿Ya tienes cuenta? Inicia sesión") } }
        ClickableText(text = annotatedString, onClick = { onGoLogin() })
    }

    if (showDatePicker) { 
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            formatter.timeZone = TimeZone.getTimeZone("UTC")
                            val formattedDate = formatter.format(Date(it))
                            registerScreenVm.onEvent(RegistrationFormEvent.FechaNacimientoChanged(formattedDate))
                        }
                        showDatePicker = false
                    }
                ) { Text("Aceptar") }
            },
            dismissButton = {
                Button(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
     }

    if (showImageOptions) {
        AlertDialog(
            onDismissRequest = { showImageOptions = false },
            title = { Text("Elegir foto de perfil") },
            text = { Text("¿Desde dónde quieres elegir la foto?") },
            confirmButton = { 
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(onClick = { 
                        showImageOptions = false
                        handleCameraClick()
                    }) { Text("Cámara") }
                    Button(onClick = { 
                        showImageOptions = false
                        galleryLauncher.launch("image/*") 
                    }) { Text("Galería") }
                }
            },
            dismissButton = { 
                TextButton(onClick = { showImageOptions = false }) { Text("Cancelar") }
            }
        )
    }
}