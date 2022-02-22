package com.ridwanstandingby.particlelife.adapters

import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters

fun ParticleLifeParameters.ShuffleForceValues.toPrefsString() =
    when (this) {
        ParticleLifeParameters.ShuffleForceValues.Always -> ALWAYS
        ParticleLifeParameters.ShuffleForceValues.Every5Minutes -> EVERY_5_MINUTES
        ParticleLifeParameters.ShuffleForceValues.EveryHour -> EVERY_HOUR
        ParticleLifeParameters.ShuffleForceValues.EveryDay -> EVERY_DAY
        ParticleLifeParameters.ShuffleForceValues.Never -> NEVER
    }

fun String.toShuffleForceValues() =
    when (this) {
        ALWAYS -> ParticleLifeParameters.ShuffleForceValues.Always
        EVERY_5_MINUTES -> ParticleLifeParameters.ShuffleForceValues.Every5Minutes
        EVERY_HOUR -> ParticleLifeParameters.ShuffleForceValues.EveryHour
        EVERY_DAY -> ParticleLifeParameters.ShuffleForceValues.EveryDay
        NEVER -> ParticleLifeParameters.ShuffleForceValues.Never
        else -> ParticleLifeParameters.ShuffleForceValues.DEFAULT
    }

private const val ALWAYS = "Always"
private const val EVERY_5_MINUTES = "Every5Minutes"
private const val EVERY_HOUR = "EveryHour"
private const val EVERY_DAY = "EveryDay"
private const val NEVER = "Never"
