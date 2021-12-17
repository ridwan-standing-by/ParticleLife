package com.ridwanstandingby.particlelife.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

val fabDiameter = 56.dp

@Composable
fun isPortrait(): Boolean =
    LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

enum class ControlPanelTab {
    PHYSICS, PARTICLES, ABOUT
}

fun ControlPanelTab.toTabNameString() =
    when (this) {
        ControlPanelTab.PHYSICS -> "Physics"// TODO
        ControlPanelTab.PARTICLES -> "Particles"// TODO
        ControlPanelTab.ABOUT -> "About"// TODO
    }

fun Float.decimal(digits: Int) = "%.${digits}f".format(this)
