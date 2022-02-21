package com.ridwanstandingby.particlelife.adapters

import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.GenerationParameters.Companion.FORCE_DISTANCE_RANGE_LOWER_DEFAULT
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.GenerationParameters.Companion.FORCE_DISTANCE_RANGE_MAX
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.GenerationParameters.Companion.FORCE_DISTANCE_RANGE_MIN
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.GenerationParameters.Companion.FORCE_DISTANCE_RANGE_UPPER_DEFAULT
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.GenerationParameters.Companion.FORCE_STRENGTH_RANGE_LOWER_DEFAULT
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.GenerationParameters.Companion.FORCE_STRENGTH_RANGE_MAX
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.GenerationParameters.Companion.FORCE_STRENGTH_RANGE_MIN
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.GenerationParameters.Companion.FORCE_STRENGTH_RANGE_UPPER_DEFAULT
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.GenerationParameters.Companion.N_PARTICLES_DEFAULT
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.GenerationParameters.Companion.N_PARTICLES_MAX
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.GenerationParameters.Companion.N_PARTICLES_MIN
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.GenerationParameters.Companion.N_SPECIES_DEFAULT
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.GenerationParameters.Companion.N_SPECIES_MAX
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.GenerationParameters.Companion.N_SPECIES_MIN
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.BECKON_ENABLED_DEFAULT
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.BECKON_PRESS_THRESHOLD_TIME_DEFAULT
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.BECKON_PRESS_THRESHOLD_TIME_MAX
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.BECKON_PRESS_THRESHOLD_TIME_MIN
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.BECKON_RADIUS_DEFAULT
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.BECKON_RADIUS_MAX
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.BECKON_RADIUS_MIN
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.BECKON_STRENGTH_DEFAULT
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.BECKON_STRENGTH_MAX
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.BECKON_STRENGTH_MIN
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.FORCE_DISTANCE_SCALE_DEFAULT
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.FORCE_DISTANCE_SCALE_MAX
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.FORCE_DISTANCE_SCALE_MIN
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.FORCE_STRENGTH_SCALE_DEFAULT
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.FORCE_STRENGTH_SCALE_MAX
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.FORCE_STRENGTH_SCALE_MIN
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.FRICTION_DEFAULT
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.FRICTION_MAX
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.FRICTION_MIN
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.HAND_OF_GOD_ENABLED_DEFAULT
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.HERD_ENABLED_DEFAULT
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.HERD_RADIUS_DEFAULT
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.HERD_RADIUS_MAX
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.HERD_RADIUS_MIN
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.HERD_STRENGTH_DEFAULT
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.HERD_STRENGTH_MAX
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.HERD_STRENGTH_MIN
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.PRESSURE_DISTANCE_DEFAULT
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.PRESSURE_DISTANCE_MAX
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.PRESSURE_DISTANCE_MIN
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.PRESSURE_STRENGTH_DEFAULT
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.PRESSURE_STRENGTH_MAX
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.PRESSURE_STRENGTH_MIN
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.TIME_SCALE_DEFAULT
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.TIME_SCALE_MAX
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters.RuntimeParameters.Companion.TIME_SCALE_MIN
import com.ridwanstandingby.particlelife.domain.Species
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

fun ParticleLifeParameters.toJson(): JSONObject =
    JSONObject().apply {
        put(GENERATION_KEY, generation.toJson())
        put(RUNTIME_KEY, runtime.toJson())
        put(SPECIES_KEY, species.toJsonArray())
    }

private fun ParticleLifeParameters.GenerationParameters.toJson(): JSONObject =
    JSONObject().apply {
        put(N_PARTICLES_KEY, nParticles)
        put(N_SPECIES_KEY, nSpecies)
        put(MAX_ATTRACTION_KEY, maxAttraction)
        put(MAX_REPULSION_KEY, maxRepulsion)
        put(FORCE_DISTANCE_LOWER_BOUND_MIN_KEY, forceDistanceLowerBoundMin)
        put(FORCE_DISTANCE_UPPER_BOUND_MAX_KEY, forceDistanceUpperBoundMax)
    }

private fun ParticleLifeParameters.RuntimeParameters.toJson(): JSONObject =
    JSONObject().apply {
        put(FORCE_STRENGTHS_KEY, forceStrengths.toJsonArray())
        put(FORCE_DISTANCE_LOWER_BOUNDS_KEY, forceDistanceLowerBounds.toJsonArray())
        put(FORCE_DISTANCE_UPPER_BOUNDS_KEY, forceDistanceUpperBounds.toJsonArray())
        put(PRESSURE_STRENGTH_KEY, pressureStrength)
        put(PRESSURE_DISTANCE_KEY, pressureDistance)
        put(FORCE_STRENGTH_SCALE_KEY, forceStrengthScale)
        put(FORCE_DISTANCE_SCALE_KEY, forceDistanceScale)
        put(FRICTION_KEY, friction)
        put(TIME_SCALE_KEY, timeScale)
        put(HAND_OF_GOD_ENABLED_KEY, handOfGodEnabled)
        put(HERD_ENABLED_KEY, herdEnabled)
        put(HERD_STRENGTH_KEY, herdStrength)
        put(HERD_RADIUS_KEY, herdRadius)
        put(BECKON_ENABLED_KEY, beckonEnabled)
        put(BECKON_PRESS_THRESHOLD_TIME_KEY, beckonPressThresholdTime)
        put(BECKON_STRENGTH_KEY, beckonStrength)
        put(BECKON_RADIUS_KEY, beckonRadius)
    }

private fun Array<Array<Double>>.toJsonArray(): JSONArray =
    JSONArray().apply {
        forEach {
            put(JSONArray(it.toDoubleArray()))
        }
    }

private fun List<Species>.toJsonArray(): JSONArray =
    JSONArray().apply {
        forEach {
            put(it.toJson())
        }
    }

private fun Species.toJson(): JSONObject =
    JSONObject().apply {
        put(COLOR_KEY, color)
    }

fun JSONObject.extractParameters(
    xMax: Double,
    yMax: Double,
    randomMatrices: Boolean
): ParticleLifeParameters {
    val generation = extractGenerationParameters()
    val runtime = extractRuntimeParameters(xMax, yMax, generation, randomMatrices = randomMatrices)
    val species = extractSpecies(generation)
    return ParticleLifeParameters(generation, runtime, species)
}

private fun JSONObject.extractGenerationParameters(): ParticleLifeParameters.GenerationParameters {

    val generation = getOrNull<JSONObject>(GENERATION_KEY)
        ?: return ParticleLifeParameters.GenerationParameters()

    val maxAttraction = generation
        .getOrDefault(MAX_ATTRACTION_KEY, FORCE_STRENGTH_RANGE_UPPER_DEFAULT)
        .bound(FORCE_STRENGTH_RANGE_MIN, FORCE_STRENGTH_RANGE_MAX)
    val maxRepulsion = generation
        .getOrDefault(MAX_REPULSION_KEY, FORCE_STRENGTH_RANGE_LOWER_DEFAULT)
        .bound(FORCE_STRENGTH_RANGE_MIN, maxAttraction)

    val forceDistanceUpperBoundMax = generation
        .getOrDefault(FORCE_DISTANCE_UPPER_BOUND_MAX_KEY, FORCE_DISTANCE_RANGE_UPPER_DEFAULT)
        .bound(FORCE_DISTANCE_RANGE_MIN, FORCE_DISTANCE_RANGE_MAX)

    val forceDistanceLowerBoundMin = generation
        .getOrDefault(FORCE_DISTANCE_LOWER_BOUND_MIN_KEY, FORCE_DISTANCE_RANGE_LOWER_DEFAULT)
        .bound(FORCE_DISTANCE_RANGE_MIN, forceDistanceUpperBoundMax)

    return ParticleLifeParameters.GenerationParameters(
        nParticles = generation
            .getOrDefault(N_PARTICLES_KEY, N_PARTICLES_DEFAULT)
            .bound(N_PARTICLES_MIN, N_PARTICLES_MAX),
        nSpecies = generation
            .getOrDefault(N_SPECIES_KEY, N_SPECIES_DEFAULT)
            .bound(N_SPECIES_MIN, N_SPECIES_MAX),
        maxAttraction = maxAttraction,
        maxRepulsion = maxRepulsion,
        forceDistanceLowerBoundMin = forceDistanceLowerBoundMin,
        forceDistanceUpperBoundMax = forceDistanceUpperBoundMax
    )
}

private fun JSONObject.extractRuntimeParameters(
    xMax: Double,
    yMax: Double,
    generation: ParticleLifeParameters.GenerationParameters,
    randomMatrices: Boolean
): ParticleLifeParameters.RuntimeParameters {

    val runtime = getOrNull<JSONObject>(RUNTIME_KEY)
        ?: return ParticleLifeParameters.RuntimeParameters.buildDefault(xMax, yMax, generation)


    val forceDistanceUpperBoundsOrNull = runtime
        .getDoubleMatrixOrNull(
            FORCE_DISTANCE_UPPER_BOUNDS_KEY,
            expectedLength = generation.nSpecies,
            min = { _, _ -> generation.forceDistanceLowerBoundMin },
            max = { _, _ -> generation.forceDistanceUpperBoundMax })

    val forceDistanceLowerBoundsOrNull = if (forceDistanceUpperBoundsOrNull != null) {
        runtime
            .getDoubleMatrixOrNull(
                FORCE_DISTANCE_LOWER_BOUNDS_KEY,
                expectedLength = generation.nSpecies,
                min = { _, _ -> generation.forceDistanceLowerBoundMin },
                max = { i, j -> forceDistanceUpperBoundsOrNull[i][j] })
    } else null

    val (forceDistanceLowerBounds, forceDistanceUpperBounds) =
        if (forceDistanceLowerBoundsOrNull != null && forceDistanceUpperBoundsOrNull != null && !randomMatrices) {
            Pair(forceDistanceLowerBoundsOrNull, forceDistanceUpperBoundsOrNull)
        } else {
            generation.generateRandomForceDistanceMatrices()
        }

    val forceStrengths = (if (randomMatrices) null else Unit)?.run {
        runtime
            .getDoubleMatrixOrNull(
                FORCE_STRENGTHS_KEY,
                expectedLength = generation.nSpecies,
                min = { _, _ -> generation.maxRepulsion },
                max = { _, _ -> generation.maxAttraction })
    } ?: generation.generateRandomForceStrengthMatrix()

    return ParticleLifeParameters.RuntimeParameters(
        xMax = xMax,
        yMax = yMax,
        forceStrengths = forceStrengths,
        forceDistanceLowerBounds = forceDistanceLowerBounds,
        forceDistanceUpperBounds = forceDistanceUpperBounds,
        pressureStrength = runtime
            .getOrDefault(PRESSURE_STRENGTH_KEY, PRESSURE_STRENGTH_DEFAULT)
            .bound(PRESSURE_STRENGTH_MIN, PRESSURE_STRENGTH_MAX),
        pressureDistance = runtime
            .getOrDefault(PRESSURE_DISTANCE_KEY, PRESSURE_DISTANCE_DEFAULT)
            .bound(PRESSURE_DISTANCE_MIN, PRESSURE_DISTANCE_MAX),
        forceStrengthScale = runtime
            .getOrDefault(FORCE_STRENGTH_SCALE_KEY, FORCE_STRENGTH_SCALE_DEFAULT)
            .bound(FORCE_STRENGTH_SCALE_MIN, FORCE_STRENGTH_SCALE_MAX),
        forceDistanceScale = runtime
            .getOrDefault(FORCE_DISTANCE_SCALE_KEY, FORCE_DISTANCE_SCALE_DEFAULT)
            .bound(FORCE_DISTANCE_SCALE_MIN, FORCE_DISTANCE_SCALE_MAX),
        friction = runtime
            .getOrDefault(FRICTION_KEY, FRICTION_DEFAULT)
            .bound(FRICTION_MIN, FRICTION_MAX),
        timeScale = runtime
            .getOrDefault(TIME_SCALE_KEY, TIME_SCALE_DEFAULT)
            .bound(TIME_SCALE_MIN, TIME_SCALE_MAX),
        handOfGodEnabled = runtime
            .getOrDefault(HAND_OF_GOD_ENABLED_KEY, HAND_OF_GOD_ENABLED_DEFAULT),
        herdEnabled = runtime
            .getOrDefault(HERD_ENABLED_KEY, HERD_ENABLED_DEFAULT),
        herdStrength = runtime
            .getOrDefault(HERD_STRENGTH_KEY, HERD_STRENGTH_DEFAULT)
            .bound(HERD_STRENGTH_MIN, HERD_STRENGTH_MAX),
        herdRadius = runtime
            .getOrDefault(HERD_RADIUS_KEY, HERD_RADIUS_DEFAULT)
            .bound(HERD_RADIUS_MIN, HERD_RADIUS_MAX),
        beckonEnabled = runtime
            .getOrDefault(BECKON_ENABLED_KEY, BECKON_ENABLED_DEFAULT),
        beckonPressThresholdTime = runtime
            .getOrDefault(BECKON_PRESS_THRESHOLD_TIME_KEY, BECKON_PRESS_THRESHOLD_TIME_DEFAULT)
            .bound(BECKON_PRESS_THRESHOLD_TIME_MIN, BECKON_PRESS_THRESHOLD_TIME_MAX),
        beckonStrength = runtime
            .getOrDefault(BECKON_STRENGTH_KEY, BECKON_STRENGTH_DEFAULT)
            .bound(BECKON_STRENGTH_MIN, BECKON_STRENGTH_MAX),
        beckonRadius = runtime
            .getOrDefault(BECKON_RADIUS_KEY, BECKON_RADIUS_DEFAULT)
            .bound(BECKON_RADIUS_MIN, BECKON_RADIUS_MAX),
    )
}

private fun JSONObject.extractSpecies(generation: ParticleLifeParameters.GenerationParameters): List<Species> {
    val speciesArray = getOrNull<JSONArray>(SPECIES_KEY) ?: return generation.generateSpecies()

    val result = mutableListOf<Species>()
    return try {
        if (speciesArray.length() != generation.nSpecies) return generation.generateSpecies()
        (0 until generation.nSpecies).forEach { i ->
            val speciesJson = speciesArray.getJSONObject(i)
            result.add(
                Species(
                    color = speciesJson.getOrDefault(
                        COLOR_KEY,
                        generation.generateSpecies()[i].color
                    )
                )
            )
        }
        result
    } catch (e: JSONException) {
        generation.generateSpecies()
    }
}

private fun <T : Comparable<T>> T.bound(min: T, max: T): T =
    when {
        this <= min -> min
        this > max -> max
        else -> this
    }

private fun JSONObject.getDoubleMatrixOrNull(
    key: String,
    expectedLength: Int,
    min: (Int, Int) -> Double,
    max: (Int, Int) -> Double
): Array<Array<Double>>? {
    val jsonMatrix = getOrNull<JSONArray>(key) ?: return null

    val result = Array(expectedLength) { Array(expectedLength) { 0.0 } }

    return try {
        if (jsonMatrix.length() != expectedLength) return null
        (0 until expectedLength).forEach { i ->
            if (jsonMatrix.getJSONArray(i).length() != expectedLength) return null
            (0 until expectedLength).forEach { j ->
                result[i][j] = jsonMatrix.getJSONArray(i).getDouble(j).bound(min(i, j), max(i, j))
            }
        }
        result
    } catch (e: JSONException) {
        null
    }
}

private inline fun <reified T : Any> JSONObject.getOrDefault(key: String, default: T): T =
    getOrNull(key) ?: default

private inline fun <reified T : Any> JSONObject.getOrNull(key: String): T? =
    try {
        when (T::class) {
            Int::class -> getInt(key) as T
            Double::class -> getDouble(key) as T
            Boolean::class -> getBoolean(key) as T
            JSONArray::class -> getJSONArray(key) as T
            JSONObject::class -> getJSONObject(key) as T
            else -> throw IllegalArgumentException("Invalid type parameter ${T::class}")
        }
    } catch (e: JSONException) {
        null
    }

private const val GENERATION_KEY = "generationParameters"
private const val N_PARTICLES_KEY = "nParticles"
private const val N_SPECIES_KEY = "nSpecies"
private const val MAX_ATTRACTION_KEY = "maxAttraction"
private const val MAX_REPULSION_KEY = "maxRepulsion"
private const val FORCE_DISTANCE_LOWER_BOUND_MIN_KEY = "forceDistanceLowerBoundMin"
private const val FORCE_DISTANCE_UPPER_BOUND_MAX_KEY = "forceDistanceUpperBoundMax"

private const val RUNTIME_KEY = "runtimeParameters"
private const val FORCE_STRENGTHS_KEY = "forceStrengths"
private const val FORCE_DISTANCE_LOWER_BOUNDS_KEY = "forceDistanceLowerBounds"
private const val FORCE_DISTANCE_UPPER_BOUNDS_KEY = "forceDistanceUpperBounds"
private const val PRESSURE_STRENGTH_KEY = "pressureStrength"
private const val PRESSURE_DISTANCE_KEY = "pressureDistance"
private const val FORCE_STRENGTH_SCALE_KEY = "forceStrengthScale"
private const val FORCE_DISTANCE_SCALE_KEY = "forceDistanceScale"
private const val FRICTION_KEY = "friction"
private const val TIME_SCALE_KEY = "timeScale"
private const val HAND_OF_GOD_ENABLED_KEY = "handOfGodEnabled"
private const val HERD_ENABLED_KEY = "herdEnabled"
private const val HERD_STRENGTH_KEY = "herdStrength"
private const val HERD_RADIUS_KEY = "herdRadius"
private const val BECKON_ENABLED_KEY = "beckonEnabled"
private const val BECKON_PRESS_THRESHOLD_TIME_KEY = "beckonPressThresholdTime"
private const val BECKON_STRENGTH_KEY = "beckonStrength"
private const val BECKON_RADIUS_KEY = "beckonRadius"

private const val SPECIES_KEY = "species"
private const val COLOR_KEY = "color"
