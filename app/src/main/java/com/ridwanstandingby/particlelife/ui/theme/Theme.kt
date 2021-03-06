package com.ridwanstandingby.particlelife.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Cyan800,
    secondary = Cyan600,
    background = BlueGray800,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
)

@Composable
fun ParticleLifeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
