package com.ridwanstandingby.particlelife.domain

import com.ridwanstandingby.verve.animation.AnimationInput
import com.ridwanstandingby.verve.sensor.press.PressDetector
import com.ridwanstandingby.verve.sensor.swipe.SwipeDetector

class ParticleLifeInput(
    var swipeDetector: SwipeDetector? = null,
    var pressDetector: PressDetector? = null
) : AnimationInput() {
    fun getSwipes() = swipeDetector?.swipes
    fun getPresses(dt: Double) = pressDetector?.run {
        updatePresses(dt)
        presses
    }
}
