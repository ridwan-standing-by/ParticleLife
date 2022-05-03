package com.ridwanstandingby.particlelife.data.prefs

import com.ridwanstandingby.particlelife.wallpaper.WallpaperMode

fun PreferencesManager.migrateIfNecessary(targetVersion: Int) {
    while (prefsVersion < targetVersion) {
        when (prefsVersion) {
            0 -> migrateTo1()
        }
    }
}

private fun PreferencesManager.migrateTo1() {
    wallpaperMode = if (prefs.getBoolean("WallpaperRandomise", false)) {
        WallpaperMode.Randomise
    } else {
        WallpaperMode.DEFAULT
    }

    prefsVersion = 1
}
