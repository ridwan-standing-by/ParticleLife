package com.ridwanstandingby.particlelife.wallpaper

import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

sealed class ShuffleForceValues {
    interface Timed {
        val time: Duration
    }

    object Always : ShuffleForceValues()
    object Every5Minutes : ShuffleForceValues(), Timed {
        override val time: Duration = 5.minutes
    }

    object EveryHour : ShuffleForceValues(), Timed {
        override val time: Duration = 1.hours
    }

    object EveryDay : ShuffleForceValues(), Timed {
        override val time: Duration = 1.days
    }

    object Never : ShuffleForceValues()

    companion object {
        val DEFAULT = EveryHour
        fun all() = listOf(Always, Every5Minutes, EveryHour, EveryDay, Never)
    }
}