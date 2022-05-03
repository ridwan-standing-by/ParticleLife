package com.ridwanstandingby.particlelife.wallpaper

enum class WallpaperMode {
    Preset,
    CurrentSettings,
    Randomise;

    companion object {
        val DEFAULT = Preset
    }
}