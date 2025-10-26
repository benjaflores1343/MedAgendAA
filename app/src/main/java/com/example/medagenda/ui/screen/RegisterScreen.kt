package com.example.medagenda.ui.screen

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
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

    // --- Camera Logic ---
    val file = File(context.cacheDir, "picture.jpg")
    val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { if (it) { registerScreenVm.onEvent(RegistrationFormEvent.ProfileImageChanged(uri)) } }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { if (it) { cameraLauncher.launch(uri) } }
    )

    LaunchedEffect(key1 = Unit) {
        registerScreenVm.validationEvents.collect {
            when (it) {
                is ValidationEvent.Success -> onRegisterOkNavigateToLogin()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(state.profileImageUri ?: "https://via.placeholder.com/150"),
            contentDescription = "Avatar",
            modifier = Modifier.size(150.dp).clip(CircleShape).clickable { permissionLauncher.launch(Manifest.permission.CAMERA) },
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- User Info ---
        OutlinedTextField(value = state.nombre, onValueChange = { registerScreenVm.onEvent(RegistrationFormEvent.NombreChanged(it)) }, modifier = Modifier.fillMaxWidth(), label = { Text("Nombre") }, leadingIcon = { Icon(Icons.Default.Person, "Nombre") }, isError = state.nombreError != null)
        if (state.nombreError != null) { Text(text = state.nombreError, color = MaterialTheme.colorScheme.error) }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = state.apellido, onValueChange = { registerScreenVm.onEvent(RegistrationFormEvent.ApellidoChanged(it)) }, modifier = Modifier.fillMaxWidth(), label = { Text("Apellido") }, leadingIcon = { Icon(Icons.Default.Person, "Apellido") }, isError = state.apellidoError != null)
        if (state.apellidoError != null) { Text(text = state.apellidoError, color = MaterialTheme.colorScheme.error) }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = state.rut, onValueChange = { registerScreenVm.onEvent(RegistrationFormEvent.RutChanged(it)) }, modifier = Modifier.fillMaxWidth(), label = { Text("RUT") }, leadingIcon = { Icon(Icons.Default.Badge, "RUT") }, isError = state.rutError != null)
        if (state.rutError != null) { Text(text = state.rutError, color = MaterialTheme.colorScheme.error) }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = state.telefono, onValueChange = { registerScreenVm.onEvent(RegistrationFormEvent.TelefonoChanged(it)) }, modifier = Modifier.fillMaxWidth(), label = { Text("Teléfono") }, leadingIcon = { Icon(Icons.Default.Phone, "Teléfono") }, isError = state.telefonoError != null, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
        if (state.telefonoError != null) { Text(text = state.telefonoError, color = MaterialTheme.colorScheme.error) }
        Spacer(modifier = Modifier.height(16.dp))

        // --- Patient Info (DatePicker) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Standard height for OutlinedTextField
                .border(
                    width = 1.dp,
                    color = if (state.fechaNacimientoError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .clickable { showDatePicker = true }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Cake,
                contentDescription = "Fecha de Nacimiento",
                tint = if (state.fechaNacimientoError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = if (state.fechaNacimiento.isNotBlank()) state.fechaNacimiento else "Fecha de Nacimiento",
                color = if (state.fechaNacimiento.isNotBlank()) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (state.fechaNacimientoError != null) {
            Text(
                text = state.fechaNacimientoError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = state.direccion, onValueChange = { registerScreenVm.onEvent(RegistrationFormEvent.DireccionChanged(it)) }, modifier = Modifier.fillMaxWidth(), label = { Text("Dirección") }, leadingIcon = { Icon(Icons.Default.Home, "Dirección") }, isError = state.direccionError != null)
        if (state.direccionError != null) { Text(text = state.direccionError, color = MaterialTheme.colorScheme.error) }
        Spacer(modifier = Modifier.height(16.dp))

        // --- Auth Info ---
        OutlinedTextField(value = state.email, onValueChange = { registerScreenVm.onEvent(RegistrationFormEvent.EmailChanged(it)) }, modifier = Modifier.fillMaxWidth(), label = { Text("Email") }, leadingIcon = { Icon(Icons.Default.Email, "Email") }, isError = state.emailError != null, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
        if (state.emailError != null) { Text(text = state.emailError, color = MaterialTheme.colorScheme.error) }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = state.password, onValueChange = { registerScreenVm.onEvent(RegistrationFormEvent.PasswordChanged(it)) }, modifier = Modifier.fillMaxWidth(), label = { Text("Contraseña") }, leadingIcon = { Icon(Icons.Default.Lock, "Password") }, isError = state.passwordError != null, visualTransformation = PasswordVisualTransformation())
        if (state.passwordError != null) { Text(text = state.passwordError, color = MaterialTheme.colorScheme.error) }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = state.repeatedPassword, onValueChange = { registerScreenVm.onEvent(RegistrationFormEvent.RepeatedPasswordChanged(it)) }, modifier = Modifier.fillMaxWidth(), label = { Text("Repetir contraseña") }, leadingIcon = { Icon(Icons.Default.Lock, "Password") }, isError = state.repeatedPasswordError != null, visualTransformation = PasswordVisualTransformation())
        if (state.repeatedPasswordError != null) { Text(text = state.repeatedPasswordError, color = MaterialTheme.colorScheme.error) }
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = state.acceptedTerms, onCheckedChange = { registerScreenVm.onEvent(RegistrationFormEvent.AcceptTerms(it)) })
            Text("Acepto los términos y condiciones")
        }
        if (state.termsError != null) { Text(text = state.termsError, color = MaterialTheme.colorScheme.error) }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { registerScreenVm.onEvent(RegistrationFormEvent.Submit) }, modifier = Modifier.fillMaxWidth()) {
            Text("Registrarse")
        }

        Spacer(modifier = Modifier.height(8.dp))

        val annotatedString = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) { append("¿Ya tienes cuenta? Inicia sesión") }
        }
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
                            // Correct for timezone offset
                            val selectedDate = Date(it + TimeZone.getDefault().getOffset(it))
                            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            val formattedDate = formatter.format(selectedDate)
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
}