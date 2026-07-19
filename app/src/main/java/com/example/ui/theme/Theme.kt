package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val PrimaryBlue = Color(0xFF667EEA)
val PrimaryPurple = Color(0xFF764BA2)
val SecondaryPink = Color(0xFFF093FB)
val SecondaryRed = Color(0xFFF5576C)
val BlueAccent = Color(0xFF3B82F6)
val GreenAccent = Color(0xFF10B981)
val OrangeAccent = Color(0xFFF59E0B)

val LightBackground = Color(0xFFF8FAFF)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFF0F4FF)
val LightOnSurface = Color(0xFF2D3748)
val LightOnSurfaceVariant = Color(0xFF4A5568)

val DarkBackground = Color(0xFF1A202C)
val DarkSurface = Color(0xFF2D3748)
val DarkSurfaceVariant = Color(0xFF171923)
val DarkOnSurface = Color(0xFFF7FAFC)
val DarkOnSurfaceVariant = Color(0xFFA0AEC0)

private val LightColors = lightColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryPink,
    tertiary = BlueAccent,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = LightOnSurface,
    onSurface = LightOnSurface,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = Color(0xFFE2E8F0)
)

private val DarkColors = darkColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryPink,
    tertiary = BlueAccent,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = DarkOnSurface,
    onSurface = DarkOnSurface,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = Color(0xFF4A5568)
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disable dynamic color to keep the custom gradients
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
