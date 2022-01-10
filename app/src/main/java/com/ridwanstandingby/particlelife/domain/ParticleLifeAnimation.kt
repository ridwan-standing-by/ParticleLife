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
            val dts = dt * timeScale

            particles.forEach outer@{ a ->
                particles.forEach { b ->
                    applyInteractionForce(a, b, dts)
                }
            }

            if (handOfGodEnabled) {
                if (herdEnabled) {
                    input.getSwipes()?.forEach { applySwipeForce(it, dts) }
                }
                if (beckonEnabled) {
                    input.getPresses(dt)
                        ?.also { resolveEasterEgg(it) }
                        ?.forEach { applyPressForce(it, dts) }
                }
            }

            particles.forEach { particle ->
                applyFriction(particle, dts)
                applyForwardTimeIntegration(particle, dts)
                applyToroidalWrapping(particle)
            }
        }
        updateLock.unlock()
    }

    private inline fun ParticleLifeParameters.RuntimeParameters.applyInteractionForce(
        a: Particle,
        b: Particle,
        dts: Double
    ) {
        val xDiff = toroidalDiff(a.x, b.x, xMax)
        if (abs(xDiff) > newtonMax) return
        val yDiff = toroidalDiff(a.y, b.y, yMax)

        val distance2 = xDiff.sq() + yDiff.sq()
        if (distance2 < newtonMax2 && a !== b) {
            val distance = sqrt(distance2)
            val force = calculateInteractionForce(
                distance,
                interactionMatrix[a.speciesIndex][b.speciesIndex]
            )
            a.xv += dts * force * xDiff / distance
            a.yv += dts * force * yDiff / distance
        }
    }

    /** Degree of repulsion particle A feels from particle B */
    private inline fun ParticleLifeParameters.RuntimeParameters.calculateInteractionForce(
        distance: Double,
        interactionCharacteristic: Double
    ): Double =
        when {
            distance > newtonMax -> 0.0
            distance > newtonMin -> -interactionCharacteristic * (1.0 - abs(distance - newtonMid) / newtonSemiInterval)
            distance > fermiRange -> 0.0
            else -> fermiForceScaleByFermiRange * (fermiRange - distance)
        } * forceScale

    private inline fun ParticleLifeParameters.RuntimeParameters.applySwipeForce(
        swipe: Swipe,
        dts: Double
    ) {
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
                particle.xv += dts * herdStrength * swipeXv
                particle.yv += dts * herdStrength * swipeYv
            }
        }
    }

    private inline fun ParticleLifeParameters.RuntimeParameters.applyPressForce(
        press: Press,
        dts: Double
    ) {
        if (press.runningTime < beckonPressThresholdTimeDefault) return
        val pressX = renderer.inverseTransformX(press.screenPosition.x, press.screenPosition.y)
        val pressY = renderer.inverseTransformY(press.screenPosition.x, press.screenPosition.y)
        particles.forEach { particle ->
            val xDiff = toroidalDiff(pressX, particle.x, xMax)
            if (abs(xDiff) > beckonRadius) return@forEach
            val yDiff = toroidalDiff(pressY, particle.y, yMax)

            val distance2 = xDiff.sq() + yDiff.sq()
            if (distance2 < beckonRadius * beckonRadius) {
                particle.xv += dts * beckonStrength * xDiff
                particle.yv += dts * beckonStrength * yDiff
            }
        }
    }

    private inline fun ParticleLifeParameters.RuntimeParameters.applyFriction(
        particle: Particle,
        dts: Double
    ) {
        particle.xv *= (1 - friction * dts)
        particle.yv *= (1 - friction * dts)
    }

    private inline fun applyForwardTimeIntegration(
        particle: Particle,
        dts: Double
    ) {
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
