package com.ridwanstandingby.particlelife.domain

import com.ridwanstandingby.verve.animation.Animation
import kotlin.math.cos
import kotlin.math.sin

class ParticleLifeAnimation(
    parameters: ParticleLifeParameters,
    renderer: ParticleLifeRenderer,
    input: ParticleLifeInput
) : Animation<ParticleLifeParameters, ParticleLifeRenderer, ParticleLifeInput>(
    parameters,
    renderer,
    input
) {

    private val particles = parameters.generateRandomParticles()

    init {
        renderer.getParticles = { particles }
    }

    var t: Double = 0.0
    override fun update(dt: Double) {
        t += dt
        particles.forEach {
            it.x += dt * 100.0 * sin(t)
            it.y += dt * 100.0 * cos(t)
        }
    }
}
