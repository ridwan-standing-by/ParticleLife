package com.ridwanstandingby.particlelife.domain

import android.graphics.Color
import android.graphics.Paint
import com.ridwanstandingby.verve.animation.Animation
import com.ridwanstandingby.verve.animation.AnimationParameters
import com.ridwanstandingby.verve.math.sq
import com.ridwanstandingby.verve.math.toroidalDiff
import java.util.concurrent.locks.ReentrantLock
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
        updateLock.unlock()
    }

    /** Degree of repulsion particle A feels from particle B */
    private fun ParticleLifeParameters.RuntimeParameters.interactionForce(
        distance: Double,
        interactionCharacteristic: Double
    ): Double =
        when {
            distance > newtonMax -> 0.0
            distance > newtonMin -> -interactionCharacteristic * (1.0 - abs(distance - newtonMid) / newtonSemiInterval)
            distance > fermiRange -> 0.0
            else -> fermiForceScaleByFermiRange * (fermiRange - distance)
        } * forceScale
}

class ParticleLifeParameters(
    var generation: GenerationParameters,
    var runtime: RuntimeParameters,
    var species: List<Species>,
    var initialParticles: List<Particle>
) : AnimationParameters() {

    data class GenerationParameters(
        var nParticles: Int = N_PARTICLES_DEFAULT,
        var nSpecies: Int = N_SPECIES_DEFAULT,
        var maxAttraction: Double = FORCE_VALUE_RANGE_UPPER_DEFAULT,
        var maxRepulsion: Double = FORCE_VALUE_RANGE_LOWER_DEFAULT
    ) {
        fun generateRandomSpecies(): List<Species> =
            listOf(
                Species(Color.RED),
                Species(Color.YELLOW),
                Species(Color.GREEN),
                Species(Color.CYAN),
                Species(Color.BLUE),
                Species(Color.MAGENTA),
                Species(-0x00C0FF91), // Purple
                Species(-0x0090C100), // Brown
                Species(-0x00909091), // Gray
                Species(Color.WHITE)
            ).take(
                when {
                    nSpecies < N_SPECIES_MIN -> N_SPECIES_MIN
                    nSpecies > N_SPECIES_MAX -> N_SPECIES_MAX
                    else -> nSpecies
                }
            )

        private fun generateRandomInteractionValue() =
            try {
                Random.nextDouble(maxRepulsion, maxAttraction)
            } catch (e: Exception) {
                maxRepulsion
            }

        private fun generateRandomInteractionVector() =
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
            const val N_PARTICLES_DEFAULT = 600
            const val N_PARTICLES_MIN = 50
            const val N_PARTICLES_MAX = 1200

            const val N_SPECIES_DEFAULT = 6
            const val N_SPECIES_MIN = 1
            const val N_SPECIES_MAX = 10

            const val FORCE_VALUE_RANGE_LOWER_DEFAULT = -1.0
            const val FORCE_VALUE_RANGE_UPPER_DEFAULT = 1.0
            const val FORCE_VALUE_RANGE_MIN = -2.0
            const val FORCE_VALUE_RANGE_MAX = 2.0
        }
    }

    class RuntimeParameters(
        var xMax: Double,
        var yMax: Double,
        val interactionMatrix: Array<Array<Double>>,
        fermiForceScale: Double = PRESSURE_DEFAULT,
        fermiRange: Double = PRESSURE_RANGE_DEFAULT,
        newtonMax: Double = FORCE_RANGE_DEFAULT,
        newtonMin: Double = FORCE_HORIZON_DEFAULT,
        var forceScale: Double = FORCE_STRENGTH_DEFAULT,
        var friction: Double = FRICTION_DEFAULT,
        var timeScale: Double = TIME_STEP_DEFAULT,
        var handOfGodEnabled: Boolean = HAND_OF_GOD_ENABLED_DEFAULT,
        var herdEnabled: Boolean = HERD_ENABLED_DEFAULT,
        var herdStrength: Double = HERD_STRENGTH_DEFAULT,
        var herdRadius: Double = HERD_RADIUS_DEFAULT,
        var beckonEnabled: Boolean = BECKON_ENABLED_DEFAULT,
        var beckonStrength: Double = BECKON_STRENGTH_DEFAULT,
        var beckonRadius: Double = BECKON_RADIUS_DEFAULT
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

        fun copy(interactionMatrix: Array<Array<Double>> = this.interactionMatrix) =
            RuntimeParameters(
                xMax = xMax,
                yMax = yMax,
                interactionMatrix = interactionMatrix,
                fermiForceScale = fermiForceScale,
                fermiRange = fermiRange,
                newtonMax = newtonMax,
                newtonMin = newtonMin,
                forceScale = forceScale,
                friction = friction,
                timeScale = timeScale,
                handOfGodEnabled = handOfGodEnabled,
                herdEnabled = herdEnabled,
                herdStrength = herdStrength,
                herdRadius = herdRadius,
                beckonEnabled = beckonEnabled,
                beckonStrength = beckonStrength,
                beckonRadius = beckonRadius
            )

        private fun recompute() {
            newtonMax2 = newtonMax.sq()
            newtonMid = (newtonMax + newtonMin) / 2
            newtonSemiInterval = newtonMax - newtonMid
            fermiForceScaleByFermiRange = fermiForceScale / fermiRange
        }

        fun randomise() {
            friction = Random.nextDouble(FRICTION_MIN, FRICTION_MAX)
            newtonMax = Random.nextDouble(FORCE_RANGE_MIN, FORCE_RANGE_MAX)
            forceScale = Random.nextDouble(FORCE_STRENGTH_MIN, FORCE_STRENGTH_MAX)
            fermiForceScale = Random.nextDouble(PRESSURE_MIN, PRESSURE_MAX)
        }

        fun reset() {
            fermiForceScale = PRESSURE_DEFAULT
            fermiRange = PRESSURE_RANGE_DEFAULT
            newtonMax = FORCE_RANGE_DEFAULT
            newtonMin = FORCE_HORIZON_DEFAULT
            forceScale = FORCE_STRENGTH_DEFAULT
            friction = FRICTION_DEFAULT
            timeScale = TIME_STEP_DEFAULT
        }

        companion object {
            const val FRICTION_DEFAULT = 0.5
            const val FRICTION_MIN = 0.0
            const val FRICTION_MAX = 5.0

            const val FORCE_STRENGTH_DEFAULT = 100.0
            const val FORCE_STRENGTH_MIN = 1.0
            const val FORCE_STRENGTH_MAX = 500.0

            const val FORCE_RANGE_DEFAULT = 80.0
            const val FORCE_RANGE_MIN = 20.0
            const val FORCE_RANGE_MAX = 200.0

            const val FORCE_HORIZON_DEFAULT = 16.0

            const val PRESSURE_DEFAULT = 100.0
            const val PRESSURE_MIN = 1.0
            const val PRESSURE_MAX = 250.0

            const val PRESSURE_RANGE_DEFAULT = 16.0

            const val TIME_STEP_DEFAULT = 1.0
            const val TIME_STEP_MIN = 0.1
            const val TIME_STEP_MAX = 3.0

            const val HAND_OF_GOD_ENABLED_DEFAULT = false

            const val HERD_ENABLED_DEFAULT = true

            const val HERD_STRENGTH_DEFAULT = 20.0
            const val HERD_STRENGTH_MIN = 1.0
            const val HERD_STRENGTH_MAX = 200.0

            const val HERD_RADIUS_DEFAULT = 20.0
            const val HERD_RADIUS_MIN = 1.0
            const val HERD_RADIUS_MAX = 200.0

            const val BECKON_ENABLED_DEFAULT = true

            const val BECKON_STRENGTH_DEFAULT = 20.0
            const val BECKON_STRENGTH_MIN = 1.0
            const val BECKON_STRENGTH_MAX = 200.0

            const val BECKON_RADIUS_DEFAULT = 20.0
            const val BECKON_RADIUS_MIN = 1.0
            const val BECKON_RADIUS_MAX = 200.0

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
