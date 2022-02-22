package com.ridwanstandingby.particlelife.domain

import android.graphics.Color
import com.ridwanstandingby.verve.animation.AnimationParameters
import kotlin.math.log2
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class ParticleLifeParameters(
    var generation: GenerationParameters,
    var runtime: RuntimeParameters,
    var species: List<Species>
) : AnimationParameters(maxTimeStep = MAX_TIME_STEP) {

    data class GenerationParameters(
        var nParticles: Int = N_PARTICLES_DEFAULT,
        var nSpecies: Int = N_SPECIES_DEFAULT,
        var maxAttraction: Double = FORCE_STRENGTH_RANGE_UPPER_DEFAULT,
        var maxRepulsion: Double = FORCE_STRENGTH_RANGE_LOWER_DEFAULT,
        var forceDistanceLowerBoundMin: Double = FORCE_DISTANCE_RANGE_LOWER_DEFAULT,
        var forceDistanceUpperBoundMax: Double = FORCE_DISTANCE_RANGE_UPPER_DEFAULT
    ) {
        fun generateSpecies(): List<Species> =
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

        fun generateRandomForceStrengthMatrix() =
            Array(nSpecies) {
                Array(nSpecies) {
                    try {
                        Random.nextDouble(maxRepulsion, maxAttraction)
                    } catch (e: Exception) {
                        maxRepulsion
                    }
                }
            }

        fun generateRandomForceDistanceMatrices(): Pair<Array<Array<Double>>, Array<Array<Double>>> {
            val lowerMatrix = Array(nSpecies) { Array(nSpecies) { 0.0 } }
            val upperMatrix = Array(nSpecies) { Array(nSpecies) { 0.0 } }

            (0 until nSpecies).forEach { i ->
                (0 until nSpecies).forEach { j ->
                    val midPoint = (forceDistanceLowerBoundMin + forceDistanceUpperBoundMax) / 2.0
                    val semiInterval =
                        (forceDistanceUpperBoundMax - forceDistanceLowerBoundMin) / 2.0
                    lowerMatrix[i][j] =
                        midPoint - semiInterval * (sqrt(1 - Random.nextDouble())
                            .takeIf { !it.isNaN() } ?: 1.0)
                    upperMatrix[i][j] =
                        midPoint + semiInterval * (sqrt(Random.nextDouble())
                            .takeIf { !it.isNaN() } ?: 1.0)
                }
            }

            return Pair(lowerMatrix, upperMatrix)
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
            const val N_PARTICLES_DEFAULT = 500
            const val N_PARTICLES_MIN = 50
            const val N_PARTICLES_MAX = 1000

            const val N_SPECIES_DEFAULT = 6
            const val N_SPECIES_MIN = 1
            const val N_SPECIES_MAX = 10

            const val FORCE_STRENGTH_RANGE_LOWER_DEFAULT = -1.0
            const val FORCE_STRENGTH_RANGE_UPPER_DEFAULT = 1.0
            const val FORCE_STRENGTH_RANGE_MIN = -2.0
            const val FORCE_STRENGTH_RANGE_MAX = 2.0

            const val FORCE_DISTANCE_RANGE_LOWER_DEFAULT = 50.0
            const val FORCE_DISTANCE_RANGE_UPPER_DEFAULT = 100.0
            const val FORCE_DISTANCE_RANGE_MIN = 20.0
            const val FORCE_DISTANCE_RANGE_MAX = 150.0
        }
    }

    class RuntimeParameters(
        var xMax: Double,
        var yMax: Double,
        val forceStrengths: Array<Array<Double>>,
        val forceDistanceLowerBounds: Array<Array<Double>>,
        val forceDistanceUpperBounds: Array<Array<Double>>,
        var pressureStrength: Double = PRESSURE_STRENGTH_DEFAULT,
        var pressureDistance: Double = PRESSURE_DISTANCE_DEFAULT,
        var forceStrengthScale: Double = FORCE_STRENGTH_SCALE_DEFAULT,
        var forceDistanceScale: Double = FORCE_DISTANCE_SCALE_DEFAULT,
        var friction: Double = FRICTION_DEFAULT,
        var timeScale: Double = TIME_SCALE_DEFAULT,
        var handOfGodEnabled: Boolean = HAND_OF_GOD_ENABLED_DEFAULT,
        var herdEnabled: Boolean = HERD_ENABLED_DEFAULT,
        var herdStrength: Double = HERD_STRENGTH_DEFAULT,
        var herdRadius: Double = HERD_RADIUS_DEFAULT,
        var beckonEnabled: Boolean = BECKON_ENABLED_DEFAULT,
        var beckonPressThresholdTime: Double = BECKON_PRESS_THRESHOLD_TIME_DEFAULT,
        var beckonStrength: Double = BECKON_STRENGTH_DEFAULT,
        var beckonRadius: Double = BECKON_RADIUS_DEFAULT
    ) {

        fun copy(
            forceStrengths: Array<Array<Double>> = this.forceStrengths,
            forceDistanceLowerBounds: Array<Array<Double>> = this.forceDistanceLowerBounds,
            forceDistanceUpperBounds: Array<Array<Double>> = this.forceDistanceUpperBounds
        ) = RuntimeParameters(
            xMax = xMax,
            yMax = yMax,
            forceStrengths = forceStrengths,
            forceDistanceLowerBounds = forceDistanceLowerBounds,
            forceDistanceUpperBounds = forceDistanceUpperBounds,
            pressureStrength = pressureStrength,
            pressureDistance = pressureDistance,
            forceStrengthScale = forceStrengthScale,
            forceDistanceScale = forceDistanceScale,
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

        fun randomise() {
            friction = 2.0.pow(Random.nextDouble(log2(FRICTION_MIN), log2(FRICTION_MAX)))
            forceStrengthScale =
                2.0.pow(
                    Random.nextDouble(
                        log2(FORCE_STRENGTH_SCALE_MIN),
                        log2(FORCE_STRENGTH_SCALE_MAX)
                    )
                )
            forceDistanceScale =
                2.0.pow(
                    Random.nextDouble(
                        log2(FORCE_DISTANCE_SCALE_MIN),
                        log2(FORCE_DISTANCE_SCALE_MAX)
                    )
                )
            pressureStrength =
                2.0.pow(Random.nextDouble(log2(PRESSURE_STRENGTH_MIN), log2(PRESSURE_STRENGTH_MAX)))
        }

        fun reset() {
            pressureStrength = PRESSURE_STRENGTH_DEFAULT
            pressureDistance = PRESSURE_DISTANCE_DEFAULT
            forceStrengthScale = FORCE_STRENGTH_SCALE_DEFAULT
            forceDistanceScale = FORCE_DISTANCE_SCALE_DEFAULT
            friction = FRICTION_DEFAULT
            timeScale = TIME_SCALE_DEFAULT
        }

        fun asPreset(): Preset {
            Preset.all().forEach { preset ->
                if (preset != Preset.Custom && copy().also { preset.applyPreset(it) }
                        .physicsParametersEquals(this)) {
                    return preset
                }
            }
            return Preset.Custom
        }

        private fun physicsParametersEquals(other: RuntimeParameters): Boolean {
            if (pressureStrength != other.pressureStrength) return false
            if (forceStrengthScale != other.forceStrengthScale) return false
            if (forceDistanceScale != other.forceDistanceScale) return false
            if (friction != other.friction) return false
            if (timeScale != other.timeScale) return false

            return true
        }

        sealed class Preset {

            abstract fun applyPreset(runtimeParameters: RuntimeParameters)

            object BalancedChaos : Preset() {
                override fun applyPreset(runtimeParameters: RuntimeParameters) {
                    with(runtimeParameters) {
                        reset()
                    }
                }
            }

            object LittleCreatures : Preset() {
                override fun applyPreset(runtimeParameters: RuntimeParameters) {
                    with(runtimeParameters) {
                        reset()
                        forceStrengthScale *= 3.0
                        forceDistanceScale *= 0.5
                        pressureStrength *= 2.0
                    }
                }
            }

            object LargeCreatures : Preset() {
                override fun applyPreset(runtimeParameters: RuntimeParameters) {
                    with(runtimeParameters) {
                        reset()
                        forceStrengthScale *= 0.5
                        forceDistanceScale *= 2.0
                    }
                }
            }

            object Behemoths : Preset() {
                override fun applyPreset(runtimeParameters: RuntimeParameters) {
                    with(runtimeParameters) {
                        reset()
                        friction = 0.06
                        forceStrengthScale *= 2.0
                        forceDistanceScale *= 3.0
                    }
                }
            }

            object Custom : Preset() {
                override fun applyPreset(runtimeParameters: RuntimeParameters) {
                    /* do nothing */
                }
            }

            companion object {
                fun default(): Preset = BalancedChaos
                fun all() =
                    listOf(BalancedChaos, LittleCreatures, LargeCreatures, Behemoths, Custom)
            }
        }

        companion object {
            fun buildDefault(
                xMax: Double,
                yMax: Double,
                generationParameters: GenerationParameters
            ): RuntimeParameters {
                val forceStrengths = generationParameters.generateRandomForceStrengthMatrix()
                val (forceDistanceLowerBounds, forceDistanceUpperBounds) = generationParameters.generateRandomForceDistanceMatrices()
                return RuntimeParameters(
                    xMax = xMax,
                    yMax = yMax,
                    forceStrengths = forceStrengths,
                    forceDistanceLowerBounds = forceDistanceLowerBounds,
                    forceDistanceUpperBounds = forceDistanceUpperBounds
                )
            }

            const val FRICTION_DEFAULT = 0.02
            const val FRICTION_MIN = 0.001
            const val FRICTION_MAX = 0.4

            const val FORCE_STRENGTH_SCALE_DEFAULT = 1.0
            const val FORCE_STRENGTH_SCALE_MIN = 0.25
            const val FORCE_STRENGTH_SCALE_MAX = 4.0

            const val FORCE_DISTANCE_SCALE_DEFAULT = 1.0
            const val FORCE_DISTANCE_SCALE_MIN = 0.25
            const val FORCE_DISTANCE_SCALE_MAX = 4.0

            const val PRESSURE_STRENGTH_DEFAULT = 100.0
            const val PRESSURE_STRENGTH_MIN = 10.0
            const val PRESSURE_STRENGTH_MAX = 1000.0

            const val PRESSURE_DISTANCE_DEFAULT = 16.0
            const val PRESSURE_DISTANCE_MIN = 8.0
            const val PRESSURE_DISTANCE_MAX = 32.0

            const val TIME_SCALE_DEFAULT = 1.0
            const val TIME_SCALE_MIN = 0.25
            const val TIME_SCALE_MAX = 4.0

            const val HAND_OF_GOD_ENABLED_DEFAULT = false

            const val HERD_ENABLED_DEFAULT = true

            const val HERD_STRENGTH_DEFAULT = 2.0
            const val HERD_STRENGTH_MIN = 0.1
            const val HERD_STRENGTH_MAX = 8.0

            const val HERD_RADIUS_DEFAULT = 160.0
            const val HERD_RADIUS_MIN = 20.0
            const val HERD_RADIUS_MAX = 400.0

            const val BECKON_ENABLED_DEFAULT = true

            const val BECKON_PRESS_THRESHOLD_TIME_DEFAULT = 0.05
            const val BECKON_PRESS_THRESHOLD_TIME_MIN = 0.01
            const val BECKON_PRESS_THRESHOLD_TIME_MAX = 0.5

            const val BECKON_STRENGTH_DEFAULT = 2.0
            const val BECKON_STRENGTH_MIN = 0.1
            const val BECKON_STRENGTH_MAX = 8.0

            const val BECKON_RADIUS_DEFAULT = 400.0
            const val BECKON_RADIUS_MIN = 50.0
            const val BECKON_RADIUS_MAX = 800.0
        }
    }

    fun generateRandomParticles(): List<Particle> =
        generation.generateRandomParticles(runtime.xMax, runtime.yMax, species)

    fun copy() = ParticleLifeParameters(generation.copy(), runtime.copy(), species.toMutableList())

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

    companion object {
        fun buildDefault(
            xMax: Double,
            yMax: Double,
            generationParameters: GenerationParameters
        ): ParticleLifeParameters =
            ParticleLifeParameters(
                generation = generationParameters,
                runtime = RuntimeParameters.buildDefault(xMax, yMax, generationParameters),
                species = generationParameters.generateSpecies()
            )

        private const val MAX_TIME_STEP = 1.0 / 60.0
    }
}
