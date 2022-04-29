package com.ridwanstandingby.particlelife.wallpaper

enum class WallpaperMode {
    Preset,
    Randomise,
    CurrentSettings;

    companion object {
        val DEFAULT = Preset
    }
}