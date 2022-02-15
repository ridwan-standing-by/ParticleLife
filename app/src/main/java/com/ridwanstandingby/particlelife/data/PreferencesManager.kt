package com.ridwanstandingby.particlelife.data

import android.content.Context
import com.ridwanstandingby.particlelife.adapters.extractParameters
import com.ridwanstandingby.particlelife.adapters.toJson
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters
import org.json.JSONException
import org.json.JSONObject

class PreferencesManager(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)

    var wallpaperParameters: ParticleLifeParameters?
        get() = prefs.getString(WALLPAPER_PARAMETERS_KEY, null)
            ?.let {
                try {
                    JSONObject(it)
                } catch (e: JSONException) {
                    null
                }
            }?.extractParameters(0.0, 0.0)
        set(value) {
            prefs.edit().putString(WALLPAPER_PARAMETERS_KEY, value?.toJson().toString()).apply()
        }

    var wallpaperRandomise: Boolean
        get() = prefs.getBoolean(WALLPAPER_RANDOMISE_KEY, false)
        set(value) = prefs.edit().putBoolean(WALLPAPER_RANDOMISE_KEY, value).apply()

    companion object {
        private const val PREFS_FILE_NAME = "ParticleLifePreferences"

        private const val WALLPAPER_PARAMETERS_KEY = "WallpaperParameters"
        private const val WALLPAPER_RANDOMISE_KEY = "WallpaperRandomise"
    }
}