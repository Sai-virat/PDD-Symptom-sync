package com.example.symptomsync.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = RichOrange,
    secondary = SmoothPurple,
    tertiary = HealthGreen,
    background = WellnessDarkCharcoal,
    surface = WellnessDarkCard,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = WellnessWarmWhite,
    onSurface = WellnessWarmWhite
)

private val LightColorScheme = lightColorScheme(
    primary = RichOrange,
    secondary = SmoothPurple,
    tertiary = HealthGreen,
    background = WellnessWarmWhite,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = WellnessDarkNavy,
    onSurface = WellnessDarkNavy
)

@Composable
fun SymptomsyncTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
