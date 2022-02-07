@file:Suppress("nothing_to_inline")

package com.ridwanstandingby.particlelife.domain

import android.graphics.Paint
import com.ridwanstandingby.verve.animation.Animation
import com.ridwanstandingby.verve.math.sq
import com.ridwanstandingby.verve.math.toroidalDiff
import com.ridwanstandingby.verve.sensor.press.Press
import com.ridwanstandingby.verve.sensor.swipe.Swipe
import java.util.concurrent.locks.ReentrantLock
import kotlin.math.abs
import kotlin.math.sqrt

class ParticleLifeAnimation(
    parameters: ParticleLifeParameters,
    renderer: ParticleLifeRenderer,
    input: ParticleLifeInput
) : Animation<ParticleLifeParameters, ParticleLifeRenderer, ParticleLifeInput>(
    parameters,
    renderer,
    input
) {
    private var particles = parameters.initialParticles

    init {
        renderer.getParticles = { particles }
        renderer.getSpecies = { parameters.species }
    }

    private val updateLock = ReentrantLock(true)
    fun restart(newParameters: ParticleLifeParameters) {
        updateLock.lock()
        particles = emptyList()
        renderer.getParticles = null
        renderer.getSpecies = null
        parameters.initialParticles = newParameters.initialParticles
        parameters.runtime = newParameters.runtime
        parameters.generation = newParameters.generation
        parameters.species = newParameters.species
        particles = newParameters.initialParticles
        renderer.getParticles = { particles }
        renderer.getSpecies = { parameters.species }
        updateLock.unlock()
    }

    override fun update(dt: Double) {
        updateLock.lock()

        with(parameters.runtime) {

            particles.forEach outer@{ a ->
                particles.forEach { b ->
                    applyInteractionForce(a, b)
                }
            }

            if (handOfGodEnabled) {
                if (herdEnabled) {
                    input.getSwipes()?.forEach { applySwipeForce(it) }
                }
                if (beckonEnabled) {
                    input.getPresses(dt)
                        ?.also { resolveEasterEgg(it) }
                        ?.forEach { applyPressForce(it) }
                }
            }

            particles.forEach { particle ->
                applyFriction(particle)
                applyForwardTimeIntegration(particle, dt, timeScale)
                applyToroidalWrapping(particle)
            }
        }
        updateLock.unlock()
    }

    private inline fun ParticleLifeParameters.RuntimeParameters.applyInteractionForce(
        a: Particle,
        b: Particle
    ) {
        val xDiff = toroidalDiff(a.x, b.x, xMax)
        val rMax = forceDistanceUpperBounds[a.speciesIndex][b.speciesIndex] * forceDistanceScale
        if (abs(xDiff) > rMax) return
        val yDiff = toroidalDiff(a.y, b.y, yMax)

        val distance2 = xDiff.sq() + yDiff.sq()
        if (distance2 < rMax.sq() && a !== b) {
            val distance = sqrt(distance2)
            val rMin = forceDistanceLowerBounds[a.speciesIndex][b.speciesIndex] * forceDistanceScale
            val force = when {
                distance > rMax -> 0.0
                distance > rMin -> -forceStrengths[a.speciesIndex][b.speciesIndex] *
                        (2.0 - abs(distance * 2.0 - rMax - rMin) / (rMax - rMin)) * forceStrengthScale
                distance > pressureDistance -> 0.0
                else -> pressureStrength * (1.0 - distance / pressureDistance)
            }
            a.xv += force * xDiff / distance
            a.yv += force * yDiff / distance
        }
    }

    private inline fun ParticleLifeParameters.RuntimeParameters.applySwipeForce(swipe: Swipe) {
        val swipeX = renderer.inverseTransformX(swipe.screenPosition.x, swipe.screenPosition.y)
        val swipeY = renderer.inverseTransformY(swipe.screenPosition.x, swipe.screenPosition.y)
        val swipeXv = renderer.inverseTransformDX(swipe.screenVelocity.x, swipe.screenVelocity.y)
        val swipeYv = renderer.inverseTransformDY(swipe.screenVelocity.x, swipe.screenVelocity.y)
        particles.forEach { particle ->
            val xDiff = toroidalDiff(swipeX, particle.x, xMax)
            if (abs(xDiff) > herdRadius) return@forEach
            val yDiff = toroidalDiff(swipeY, particle.y, yMax)

            val distance2 = xDiff.sq() + yDiff.sq()
            if (distance2 < herdRadius * herdRadius) {
                particle.xv += swipeXv * herdStrength * friction
                particle.yv += swipeYv * herdStrength * friction
            }
        }
    }

    private inline fun ParticleLifeParameters.RuntimeParameters.applyPressForce(press: Press) {
        if (press.runningTime < beckonPressThresholdTimeDefault) return
        val pressX = renderer.inverseTransformX(press.screenPosition.x, press.screenPosition.y)
        val pressY = renderer.inverseTransformY(press.screenPosition.x, press.screenPosition.y)
        particles.forEach { particle ->
            val xDiff = toroidalDiff(pressX, particle.x, xMax)
            if (abs(xDiff) > beckonRadius) return@forEach
            val yDiff = toroidalDiff(pressY, particle.y, yMax)

            val distance2 = xDiff.sq() + yDiff.sq()
            if (distance2 < beckonRadius * beckonRadius) {
                particle.xv += xDiff * beckonStrength * friction
                particle.yv += yDiff * beckonStrength * friction
            }
        }
    }

    private inline fun ParticleLifeParameters.RuntimeParameters.applyFriction(particle: Particle) {
        particle.xv *= 1 - friction
        particle.yv *= 1 - friction
    }

    private inline fun applyForwardTimeIntegration(
        particle: Particle,
        dt: Double,
        timeScale: Double
    ) {
        val dts = dt * timeScale
        particle.x += particle.xv * dts
        particle.y += particle.yv * dts
    }

    private inline fun ParticleLifeParameters.RuntimeParameters.applyToroidalWrapping(particle: Particle) {
        if (particle.x > xMax)
            particle.x = 0.0
        else if (particle.x < 0.0)
            particle.x = xMax
        if (particle.y > yMax)
            particle.y = 0.0
        else if (particle.y < 0.0) {
            particle.y = yMax
        }
    }

    private inline fun resolveEasterEgg(presses: MutableList<Press>) {
        if (presses.size >= 4 && presses.any { it.runningTime > 1.0 }) {
            presses.forEach { it.runningTime = 0.0 }
            renderer.easterEgg = !renderer.easterEgg
        }
    }
}

data class Particle(
    var x: Double,
    var y: Double,
    var xv: Double,
    var yv: Double,
    val speciesIndex: Int
)

data class Species(val color: Int) {
    val paint: Paint = Paint().apply { color = this@Species.color }
}
