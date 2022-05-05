package com.ridwanstandingby.particlelife.data.adapters

import com.ridwanstandingby.particlelife.wallpaper.ShuffleForceValues
import com.ridwanstandingby.particlelife.wallpaper.WallpaperMode

fun ShuffleForceValues.toPrefsString() =
    when (this) {
        ShuffleForceValues.Always -> ALWAYS
        ShuffleForceValues.Every5Minutes -> EVERY_5_MINUTES
        ShuffleForceValues.EveryHour -> EVERY_HOUR
        ShuffleForceValues.EveryDay -> EVERY_DAY
        ShuffleForceValues.Never -> NEVER
    }

fun String?.toShuffleForceValues() =
    when (this) {
        ALWAYS -> ShuffleForceValues.Always
        EVERY_5_MINUTES -> ShuffleForceValues.Every5Minutes
        EVERY_HOUR -> ShuffleForceValues.EveryHour
        EVERY_DAY -> ShuffleForceValues.EveryDay
        NEVER -> ShuffleForceValues.Never
        else -> ShuffleForceValues.DEFAULT
    }

private const val ALWAYS = "Always"
private const val EVERY_5_MINUTES = "Every5Minutes"
private const val EVERY_HOUR = "EveryHour"
private const val EVERY_DAY = "EveryDay"
private const val NEVER = "Never"

fun WallpaperMode.toPrefsString() =
    when (this) {
        WallpaperMode.Preset -> PRESET
        WallpaperMode.Randomise -> RANDOMISE
        WallpaperMode.CurrentSettings -> CURRENT_SETTINGS
    }

fun String?.toWallpaperMode() =
    when (this) {
        PRESET -> WallpaperMode.Preset
        RANDOMISE -> WallpaperMode.Randomise
        CURRENT_SETTINGS -> WallpaperMode.CurrentSettings
        else -> WallpaperMode.DEFAULT
    }

private const val PRESET = "Preset"
private const val RANDOMISE = "Randomise"
private const val CURRENT_SETTINGS = "CurrentSettings"