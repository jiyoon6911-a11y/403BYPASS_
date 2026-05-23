package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonBlue,
    onPrimary = Color.White,
    primaryContainer = SlateCardHeader,
    onPrimaryContainer = ElectricCyan,
    secondary = ElectricCyan,
    onSecondary = Color.Black,
    tertiary = SafeGreen,
    onTertiary = Color.White,
    background = SlateDark,
    onBackground = TextPrimary,
    surface = SlateCard,
    onSurface = TextPrimary,
    surfaceVariant = SlateCardHeader,
    onSurfaceVariant = Color.White,
    error = HazardRed,
    onError = Color.White
)

// A maximum-contrast scheme matching strict WCAG Triple-A black-background criteria
private val HighContrastColorScheme = darkColorScheme(
    primary = NeonBlue,
    onPrimary = Color.Black,
    primaryContainer = Color.Black,
    onPrimaryContainer = Color.White,
    secondary = Color.White,
    onSecondary = Color.Black,
    tertiary = SafeGreen,
    onTertiary = Color.Black,
    background = HighContrastBlack,
    onBackground = TextPrimaryHC,
    surface = HighContrastBlack,
    onSurface = TextPrimaryHC,
    surfaceVariant = HighContrastBlack,
    onSurfaceVariant = TextPrimaryHC,
    error = HazardRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    highContrastMode: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (highContrastMode) {
        HighContrastColorScheme
    } else {
        DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
