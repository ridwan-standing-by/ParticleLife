package com.ridwanstandingby.particlelife.adapters

import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters

fun ParticleLifeParameters.ShuffleForceValues.toPrefsString() =
    when (this) {
        ParticleLifeParameters.ShuffleForceValues.Always -> ALWAYS
        ParticleLifeParameters.ShuffleForceValues.Timed.Every5Minutes -> EVERY_5_MINUTES
        ParticleLifeParameters.ShuffleForceValues.Timed.EveryHour -> EVERY_HOUR
        ParticleLifeParameters.ShuffleForceValues.Timed.EveryDay -> EVERY_DAY
        ParticleLifeParameters.ShuffleForceValues.Never -> NEVER
    }

fun String.toShuffleForceValues() =
    when (this) {
        "Always" -> ParticleLifeParameters.ShuffleForceValues.Always
        "Every5Minutes" -> ParticleLifeParameters.ShuffleForceValues.Timed.Every5Minutes
        "EveryHour" -> ParticleLifeParameters.ShuffleForceValues.Timed.EveryHour
        "EveryDay" -> ParticleLifeParameters.ShuffleForceValues.Timed.EveryDay
        "Never" -> ParticleLifeParameters.ShuffleForceValues.Never
        else -> ParticleLifeParameters.ShuffleForceValues.DEFAULT
    }

private const val ALWAYS = "Always"
private const val EVERY_5_MINUTES = "Every5Minutes"
private const val EVERY_HOUR = "EveryHour"
private const val EVERY_DAY = "EveryDay"
private const val NEVER = "Never"
