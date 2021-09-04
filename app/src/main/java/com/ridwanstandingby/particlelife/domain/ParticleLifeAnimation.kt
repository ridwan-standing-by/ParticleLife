package com.ridwanstandingby.particlelife.domain

import android.graphics.Color
import android.graphics.Paint
import com.ridwanstandingby.verve.animation.Animation
import com.ridwanstandingby.verve.animation.AnimationParameters
import com.ridwanstandingby.verve.math.sq
import com.ridwanstandingby.verve.math.toroidalDiff
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random

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

    private var averageDt: Double = 0.0
    private var nSteps: Double = 0.0
    override fun update(dt: Double) {
        averageDt = (nSteps * averageDt + dt) / (nSteps + 1.0)
        nSteps += 1.0
        println("averageDt $averageDt")

        with(parameters) {
            val dts = dt * timeScale
            particles.forEach outer@{ a ->
                particles.forEach { b ->
                    val xDiff = toroidalDiff(a.x, b.x, parameters.xMax)
                    if (abs(xDiff) > newtonMax) return@forEach
                    val yDiff = toroidalDiff(a.y, b.y, parameters.yMax)

                    val distance2 = xDiff.sq() + yDiff.sq()
                    if (distance2 < newtonMax2 && a !== b) {
                        val distance = sqrt(distance2)
                        val force = interactionForce(
                            distance,
                            characteristicMatrix[a.species]!![b.species]!!
                        )
                        a.xv += dts * force * xDiff / distance
                        a.yv += dts * force * yDiff / distance
                    }
                }
            }

            particles.forEach { particle ->
                particle.xv *= (1 - friction * dts)
                particle.yv *= (1 - friction * dts)

                particle.x += particle.xv * dts
                particle.y += particle.yv * dts

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
        }
    }

    /** Degree of repulsion particle A feels from particle B */
    private fun ParticleLifeParameters.interactionForce(
        distance: Double,
        attractionCharacteristic: Double
    ): Double =
        when {
            distance > newtonMax -> 0.0
            distance > newtonMin -> attractionCharacteristic * (1.0 - abs(distance - newtonSemiInterval) / newtonSemiInterval)
            distance > fermiRange -> 0.0
            else -> fermiForceScaleByFermiRange * (fermiRange - distance)
        } * forceScale
}

class ParticleLifeParameters(
    val xMax: Double,
    val yMax: Double,
    val nParticles: Int = 600,
    val fermiForceScale: Double = 100.0,
    val fermiRange: Double = 16.0,
    val newtonMax: Double = 80.0,
    val newtonMin: Double = 16.0,
    val forceScale: Double = 100.0,
    val friction: Double = 0.5,
    val timeScale: Double = 1.0,
    val species: List<Species> = generateRandomSpecies(),
    val characteristicMatrix: Map<Species, Map<Species, Double>> = generateRandomForceMatrix(species)
) : AnimationParameters() {

    val newtonMax2: Double = newtonMax.sq()
    val newtonMid = (newtonMax + newtonMin) / 2
    val newtonSemiInterval = newtonMax - newtonMid

    val fermiForceScaleByFermiRange = fermiForceScale / fermiRange

    fun generateRandomParticles(): List<Particle> {
        return List(nParticles) {
            Particle(
                x = Random.nextDouble(from = 0.0, until = xMax),
                y = Random.nextDouble(from = 0.0, until = yMax),
                xv = 0.0,
                yv = 0.0,
                species = species.random()
            )
        }
    }
}

fun generateRandomSpecies(): List<Species> =
    listOf(
        Species(Color.RED),
        Species(Color.YELLOW),
        Species(Color.GREEN),
        Species(Color.CYAN),
        Species(Color.BLUE),
        Species(Color.MAGENTA)
    )

fun generateRandomForceMatrix(species: List<Species>) =
    species.associateWith {
        species.associateWith {
            Random.nextDouble(-1.0, 1.0)
        }
    }

data class Particle(
    var x: Double,
    var y: Double,
    var xv: Double,
    var yv: Double,
    val species: Species
)

data class Species(val color: Int) {
    val paint: Paint = Paint().apply { color = this@Species.color }
}
