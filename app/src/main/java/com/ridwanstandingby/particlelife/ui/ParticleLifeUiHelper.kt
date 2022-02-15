package com.ridwanstandingby.particlelife.ui

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.ridwanstandingby.particlelife.R
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters

val fabDiameter = 56.dp

@Composable
fun isPortrait(): Boolean =
    LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

enum class ControlPanelTab {
    PHYSICS, PARTICLES, WALLPAPER, ABOUT
}

enum class HandOfGodPanelMode {
    OFF, PHYSICS, WALLPAPER
}

typealias WallpaperPhysicsSetting = ParticleLifeParameters.RuntimeParameters.Preset?

val Randomise: WallpaperPhysicsSetting = null

@StringRes
fun ControlPanelTab.toTabNameString() =
    when (this) {
        ControlPanelTab.PHYSICS -> R.string.physics_title
        ControlPanelTab.PARTICLES -> R.string.particles_title
        ControlPanelTab.WALLPAPER -> R.string.wallpaper_title
        ControlPanelTab.ABOUT -> R.string.about_title
    }

fun Float.decimal(digits: Int) = "%.${digits}f".format(this)

@StringRes
fun ParticleLifeParameters.RuntimeParameters.Preset?.nameString() =
    when (this) {
        ParticleLifeParameters.RuntimeParameters.Preset.BalancedChaos -> R.string.preset_balanced_chaos
        ParticleLifeParameters.RuntimeParameters.Preset.Behemoths -> R.string.preset_behemoths
        ParticleLifeParameters.RuntimeParameters.Preset.Custom -> R.string.preset_custom
        ParticleLifeParameters.RuntimeParameters.Preset.LargeCreatures -> R.string.preset_large_creatures
        ParticleLifeParameters.RuntimeParameters.Preset.LittleCreatures -> R.string.preset_little_creatures
        null -> R.string.preset_randomise
    }
