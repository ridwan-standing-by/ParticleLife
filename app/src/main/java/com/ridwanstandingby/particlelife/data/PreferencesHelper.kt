package com.ridwanstandingby.particlelife.data

import android.annotation.SuppressLint
import android.content.SharedPreferences
import kotlin.reflect.KProperty

/**
 * Class to use as a delegated property for easy use with [SharedPreferences]
 * [getValue] returns null if the preference was never set and [defValue] is null.
 */
abstract class PrimitivePreference<T : Any>(
    private val prefs: SharedPreferences,
    private val key: String,
    private val defValue: T?,
    private val getPrimitive: SharedPreferences.() -> T?,
    private val putPrimitive: SharedPreferences.Editor.(T) -> SharedPreferences.Editor
) {
    private var value: T?
        get() = prefs.getPrimitive()
        @SuppressLint("CommitPrefEdits") // False positive
        set(value) {
            if (value != null) {
                prefs.edit().putPrimitive(value).apply()
            } else {
                prefs.edit().remove(key).apply()
            }
        }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? = value

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        this.value = value
    }
}

class StringPreference(
    prefs: SharedPreferences,
    private val key: String,
    private val defValue: String? = null
) : PrimitivePreference<String>(prefs, key, defValue,
    { getString(key, defValue) },
    { putString(key, it as String?) }
)
