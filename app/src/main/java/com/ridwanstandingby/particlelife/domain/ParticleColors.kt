package com.ridwanstandingby.particlelife.domain

import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.Color as ComposeColor

fun buildColors(n: Int) =
    SortableColor.values()
        .sortedBy { it.priority }
        .take(n)
        .sortedBy { it.order }
        .map { it.colorInt }

private enum class SortableColor(val colorInt: Int, val order: Int, val priority: Int) {
    Red700(
        ComposeColor(0xFFD32F2F).toArgb(), order = 1, priority = 1
    ),
    Orange500(
        ComposeColor(0xFFFF9800).toArgb(), order = 2, priority = 5
    ),
    Yellow500(
        ComposeColor(0xFFFFEB3B).toArgb(), order = 3, priority = 3
    ),
    LightGreen400(
        ComposeColor(0xFF9CCC65).toArgb(), order = 4, priority = 11
    ),
    Green700(
        ComposeColor(0xFF388E3C).toArgb(), order = 5, priority = 4
    ),
    Cyan300(
        ComposeColor(0xFF4DD0E1).toArgb(), order = 6, priority = 2
    ),
    Indigo500(
        ComposeColor(0xFF3F51B5).toArgb(), order = 7, priority = 7
    ),
    Purple500(
        ComposeColor(0xFF9C27B0).toArgb(), order = 8, priority = 6
    ),
    Pink300(
        ComposeColor(0xFFF06292).toArgb(), order = 9, priority = 8
    ),
    Brown400(
        ComposeColor(0xFF8D6E63).toArgb(), order = 10, priority = 9
    ),
    BlueGray300(
        ComposeColor(0xFF90A4AE).toArgb(), order = 11, priority = 10
    ),
    BlueGray50(
        ComposeColor(0xFFECEFF1).toArgb(), order = 12, priority = 12
    ),
}