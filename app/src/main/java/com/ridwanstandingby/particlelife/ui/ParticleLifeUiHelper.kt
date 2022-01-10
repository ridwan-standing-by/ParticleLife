package com.ridwanstandingby.particlelife.ui

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.ridwanstandingby.particlelife.R

val fabDiameter = 56.dp

@Composable
fun isPortrait(): Boolean =
    LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

enum class ControlPanelTab {
    PHYSICS, PARTICLES, ABOUT
}

@StringRes
fun ControlPanelTab.toTabNameString() =
    when (this) {
        ControlPanelTab.PHYSICS -> R.string.physics_title
        ControlPanelTab.PARTICLES -> R.string.particles_title
        ControlPanelTab.ABOUT -> R.string.about_title
    }

fun Float.decimal(digits: Int) = "%.${digits}f".format(this)
