package com.ridwanstandingby.particlelife.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp
import com.ridwanstandingby.particlelife.ui.fabDiameter

val Shapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(fabDiameter / 2),
    large = RoundedCornerShape(fabDiameter / 2)
)
