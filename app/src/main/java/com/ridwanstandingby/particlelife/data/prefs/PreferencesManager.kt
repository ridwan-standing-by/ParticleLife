package com.ridwanstandingby.particlelife.data.prefs

import android.content.Context
import android.content.SharedPreferences
import com.ridwanstandingby.particlelife.adapters.*
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters
import com.ridwanstandingby.particlelife.wallpaper.WallpaperMode
import org.json.JSONException
import org.json.JSONObject

class PreferencesManager(context: Context) {

    val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)

    var prefsVersion: Int
        get() = prefs.getInt(PREFS_VERSION_KEY, 0)
        set(value) = prefs.edit().putInt(PREFS_VERSION_KEY, value).apply()

    fun getWallpaperParameters(randomiseMatrices: Boolean) =
        prefs.getString(WALLPAPER_PARAMETERS_KEY, null)?.let {
            try {
                JSONObject(it)
            } catch (e: JSONException) {
                null
            }
        }?.extractParameters(0.0, 0.0, randomiseMatrices)

    fun setWallpaperParameters(value: ParticleLifeParameters?) =
        prefs.edit().putString(WALLPAPER_PARAMETERS_KEY, value?.toJson().toString())
            .apply()

    var wallpaperMode: WallpaperMode
        get() = prefs.getString(WALLPAPER_MODE_KEY, null).toWallpaperMode()
        set(value) = prefs.edit().putString(WALLPAPER_MODE_KEY, value.toPrefsString()).apply()

    var wallpaperShuffleForceValues: ParticleLifeParameters.ShuffleForceValues
        get() = prefs.getString(WALLPAPER_SHUFFLE_FORCE_VALUES_KEY, null).toShuffleForceValues()
        set(value) = prefs.edit()
            .putString(WALLPAPER_SHUFFLE_FORCE_VALUES_KEY, value.toPrefsString()).apply()

    var wallpaperParametersChanged: Boolean
        get() = prefs.getBoolean(WALLPAPER_PARAMETERS_CHANGED_KEY, false)
        set(value) = prefs.edit().putBoolean(WALLPAPER_PARAMETERS_CHANGED_KEY, value).apply()

    init {
        migrateIfNecessary(targetVersion = PREFS_VERSION)
    }

    companion object {
        private const val PREFS_FILE_NAME = "ParticleLifePreferences"
        private const val PREFS_VERSION_KEY = "PreferencesVersion"
        private const val PREFS_VERSION = 1

        private const val WALLPAPER_PARAMETERS_KEY = "WallpaperParameters"
        private const val WALLPAPER_MODE_KEY = "WallpaperMode"
        private const val WALLPAPER_SHUFFLE_FORCE_VALUES_KEY = "WallpaperShuffleForceValues"
        private const val WALLPAPER_PARAMETERS_CHANGED_KEY = "WallpaperParametersChanged"
    }
}