package com.baila.spotify

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BailaObsidian = Color(0xFF08080E)
val BailaCardSurface = Color(0xFF131320)
val BailaCardBorder = Color(0xFF25253A)
val BailaNeonPink = Color(0xFFFF2E93)
val BailaElectricCyan = Color(0xFF00E5FF)
val SpotifyGreen = Color(0xFF1DB954)
val BailaTextPrimary = Color(0xFFF8F8FC)
val BailaTextSecondary = Color(0xFF9C9CB8)

private val BailaDarkColorScheme = darkColorScheme(
    primary = BailaNeonPink,
    secondary = BailaElectricCyan,
    tertiary = SpotifyGreen,
    background = BailaObsidian,
    surface = BailaCardSurface,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = BailaTextPrimary,
    onSurface = BailaTextPrimary
)

@Composable
fun BailaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BailaDarkColorScheme,
        content = content
    )
}
