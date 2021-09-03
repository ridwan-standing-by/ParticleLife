package com.ridwanstandingby.particlelife.domain

import android.graphics.Color
import com.ridwanstandingby.verve.animation.AnimationParameters
import kotlin.random.Random

class ParticleLifeParameters(val width: Double, val height: Double) : AnimationParameters() {

    fun generateRandomParticles(): List<Particle> {
        val species = ParticleSpecies(Color.CYAN)
        return List(600) {
            Particle(
                x = Random.nextDouble(from = 20.0, until = 1000.0),
                y = Random.nextDouble(from = 50.0, until = 1900.0),
                species = species
            )
        }
    }
}