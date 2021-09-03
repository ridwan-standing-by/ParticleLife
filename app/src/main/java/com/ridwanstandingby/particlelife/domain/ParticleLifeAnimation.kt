package com.ridwanstandingby.particlelife.domain

import com.ridwanstandingby.verve.animation.Animation

class ParticleLifeAnimation(
    parameters: ParticleLifeParameters,
    renderer: ParticleLifeRenderer,
    input: ParticleLifeInput
) : Animation<ParticleLifeParameters, ParticleLifeRenderer, ParticleLifeInput>(
    parameters,
    renderer,
    input
) {

    override fun update(dt: Double) {
        // TODO("Not yet implemented")
    }
}
