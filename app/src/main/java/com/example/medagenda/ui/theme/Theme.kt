package com.example.medagenda.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryLight,
    onPrimaryContainer = OnBackground,
    
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryLight,
    onSecondaryContainer = OnBackground,
    
    tertiary = Info,
    onTertiary = OnPrimary,
    tertiaryContainer = SecondaryLight,
    onTertiaryContainer = OnBackground,
    
    background = Background,
    onBackground = OnBackground,
    
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurface,
    
    error = Error,
    onError = OnError,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    
    outline = PrimaryDark,
    outlineVariant = PrimaryLight,
    
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFF2F3033),
    inverseOnSurface = Color(0xFFF1F0F4),
    inversePrimary = PrimaryLight,
    
    surfaceDim = Color(0xFFDDD8DD),
    surfaceBright = Color(0xFFF8FAFF),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF1F0F4),
    surfaceContainer = Color(0xFFEBE8ED),
    surfaceContainerHigh = Color(0xFFE5E2E9),
    surfaceContainerHighest = Color(0xFFDFDCE3),
)

@Composable
fun MedAgendaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
