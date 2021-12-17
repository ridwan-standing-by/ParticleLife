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
    var particles = parameters.initialParticles

    init {
        renderer.getParticles = { particles }
        renderer.getSpecies = { parameters.species }
    }

    private var averageDt: Double = 0.0
    private var nSteps: Double = 0.0
    override fun update(dt: Double) {
        averageDt = (nSteps * averageDt + dt) / (nSteps + 1.0)
        nSteps += 1.0
        println("averageDt $averageDt")

        with(parameters.runtime) {
            val dts = dt * timeScale
            particles.forEach outer@{ a ->
                particles.forEach { b ->
                    val xDiff = toroidalDiff(a.x, b.x, xMax)
                    if (abs(xDiff) > newtonMax) return@forEach
                    val yDiff = toroidalDiff(a.y, b.y, yMax)

                    val distance2 = xDiff.sq() + yDiff.sq()
                    if (distance2 < newtonMax2 && a !== b) {
                        val distance = sqrt(distance2)
                        val force = interactionForce(
                            distance,
                            interactionMatrix[a.speciesIndex][b.speciesIndex]
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
    private fun ParticleLifeParameters.RuntimeParameters.interactionForce(
        distance: Double,
        interactionCharacteristic: Double
    ): Double =
        when {
            distance > newtonMax -> 0.0
            distance > newtonMin -> interactionCharacteristic * (1.0 - abs(distance - newtonMid) / newtonSemiInterval)
            distance > fermiRange -> 0.0
            else -> fermiForceScaleByFermiRange * (fermiRange - distance)
        } * forceScale
}

class ParticleLifeParameters(
    val generation: GenerationParameters,
    val runtime: RuntimeParameters,
    val species: List<Species>,
    val initialParticles: List<Particle>
) : AnimationParameters() {

    data class GenerationParameters(
        var nParticles: Int = 600,
        var nSpecies: Int = 6,
        var maxRepulsion: Double = 1.0,
        var maxAttraction: Double = -1.0
    ) {
        fun generateRandomSpecies(): List<Species> =
            listOf(
                Species(Color.RED),
                Species(Color.YELLOW),
                Species(Color.GREEN),
                Species(Color.CYAN),
                Species(Color.BLUE),
                Species(Color.MAGENTA),
                Species(Color.GRAY),
                Species(Color.WHITE)
            ).take(
                when {
                    nSpecies < MIN_SPECIES -> MIN_SPECIES
                    nSpecies > MAX_SPECIES -> MAX_SPECIES
                    else -> nSpecies
                }
            )

        fun generateRandomInteractionValue() =
            Random.nextDouble(maxAttraction, maxRepulsion)

        fun generateRandomInteractionVector() =
            Array(nSpecies) {
                generateRandomInteractionValue()
            }

        fun generateRandomInteractionMatrix() =
            Array(nSpecies) {
                generateRandomInteractionVector()
            }

        fun generateRandomParticles(
            xMax: Double,
            yMax: Double,
            species: List<Species>
        ): List<Particle> {
            return List(nParticles) {
                Particle(
                    x = Random.nextDouble(from = 0.0, until = xMax),
                    y = Random.nextDouble(from = 0.0, until = yMax),
                    xv = 0.0,
                    yv = 0.0,
                    speciesIndex = species.indices.random()
                )
            }
        }

        companion object {
            const val MIN_SPECIES = 1
            const val MAX_SPECIES = 8
        }
    }

    class RuntimeParameters(
        var xMax: Double,
        var yMax: Double,
        val interactionMatrix: Array<Array<Double>>,
        fermiForceScale: Double = 100.0,
        fermiRange: Double = 16.0,
        newtonMax: Double = 80.0,
        newtonMin: Double = 16.0,
        var forceScale: Double = 100.0,
        var friction: Double = 0.5,
        var timeScale: Double = 1.0
    ) {

        var fermiForceScale: Double = fermiForceScale
            set(value) {
                field = value
                recompute()
            }
        var fermiRange = fermiRange
            set(value) {
                field = value
                recompute()
            }
        var newtonMax = newtonMax
            set(value) {
                field = value
                recompute()
            }
        var newtonMin = newtonMin
            set(value) {
                field = value
                recompute()
            }

        var newtonMax2: Double = 0.0
            private set
        var newtonMid: Double = 0.0
            private set
        var newtonSemiInterval: Double = 0.0
            private set
        var fermiForceScaleByFermiRange: Double = 0.0
            private set

        init {
            recompute()
        }

        fun copy() = RuntimeParameters(
            xMax = xMax,
            yMax = yMax,
            interactionMatrix = interactionMatrix,
            fermiForceScale = fermiForceScale,
            fermiRange = fermiRange,
            newtonMax = newtonMax,
            newtonMin = newtonMin,
            forceScale = forceScale,
            friction = friction,
            timeScale = timeScale
        )

        private fun recompute() {
            newtonMax2 = newtonMax.sq()
            newtonMid = (newtonMax + newtonMin) / 2
            newtonSemiInterval = newtonMax - newtonMid
            fermiForceScaleByFermiRange = fermiForceScale / fermiRange
        }
    }

    companion object {
        fun buildDefault(xMax: Double, yMax: Double): ParticleLifeParameters {
            val generationParameters = GenerationParameters()
            val species = generationParameters.generateRandomSpecies()
            val interactionMatrix = generationParameters.generateRandomInteractionMatrix()
            val initialParticles = generationParameters.generateRandomParticles(xMax, yMax, species)
            return ParticleLifeParameters(
                generation = generationParameters,
                runtime = RuntimeParameters(xMax, yMax, interactionMatrix),
                species = species,
                initialParticles = initialParticles
            )
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
