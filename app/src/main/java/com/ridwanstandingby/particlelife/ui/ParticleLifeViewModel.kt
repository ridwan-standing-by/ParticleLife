package com.ridwanstandingby.particlelife.ui

import android.content.res.Resources
import android.graphics.Bitmap
import android.view.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.lifecycle.ViewModel
import com.ridwanstandingby.particlelife.data.PreferencesManager
import com.ridwanstandingby.particlelife.domain.ParticleLifeAnimation
import com.ridwanstandingby.particlelife.domain.ParticleLifeInput
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters
import com.ridwanstandingby.particlelife.domain.ParticleLifeRenderer
import com.ridwanstandingby.particlelife.logging.Log
import com.ridwanstandingby.verve.animation.AnimationRunner
import com.ridwanstandingby.verve.math.FloatVector2
import com.ridwanstandingby.verve.sensor.press.PressDetector
import com.ridwanstandingby.verve.sensor.swipe.SwipeDetector

class ParticleLifeViewModel(
    val animationRunner: AnimationRunner,
    private val prefs: PreferencesManager,
    easterBitmap: Bitmap? = null
) : ViewModel() {

    val controlPanelExpanded = mutableStateOf(false)
    val selectedTabIndex = mutableStateOf(0)
    val selectedPreset = mutableStateOf(ParticleLifeParameters.RuntimeParameters.Preset.default())
    val editForceStrengthsPanelExpanded = mutableStateOf(false)
    val editForceStrengthsSelectedSpeciesIndex = mutableStateOf(0)
    val editForceDistancesPanelExpanded = mutableStateOf(false)
    val editForceDistancesSelectedSpeciesIndex = mutableStateOf(0)
    val editHandOfGodPanelExpanded = mutableStateOf(HandOfGodPanelMode.OFF)
    val selectedWallpaperPhysics = mutableStateOf(
        if (prefs.wallpaperRandomise) Randomise else prefs.getWallpaperParameters(randomiseMatrices = false)?.runtime?.asPreset()
            ?: WallpaperPhysicsSetting.default()
    )
    val wallpaperShuffleForceValues = mutableStateOf(prefs.wallpaperShuffleForceValues)

    val parameters = mutableStateOf(
        ParticleLifeParameters.buildDefault(
            Resources.getSystem().displayMetrics.widthPixels.toDouble(),
            Resources.getSystem().displayMetrics.heightPixels.toDouble(),
            ParticleLifeParameters.GenerationParameters()
        ), neverEqualPolicy()
    )

    private fun updateParameters(block: ParticleLifeParameters.() -> Unit) {
        parameters.value = parameters.value.apply(block)
    }

    val wallpaperParameters = mutableStateOf(
        prefs.getWallpaperParameters(randomiseMatrices = false)
            ?: ParticleLifeParameters.buildDefault(
                0.0,
                0.0,
                ParticleLifeParameters.GenerationParameters()
            ), neverEqualPolicy()
    )

    private val renderer = ParticleLifeRenderer(easterBitmap = easterBitmap)

    private val input = ParticleLifeInput()

    private lateinit var animation: ParticleLifeAnimation
    fun start(swipeDetector: SwipeDetector, pressDetector: PressDetector) {
        Log.i("ParticleLifeViewModel::started")
        input.swipeDetector = swipeDetector
        input.pressDetector = pressDetector
        animationRunner.start(
            ParticleLifeAnimation(parameters.value, renderer, input).also { animation = it }
        )
    }

    fun onViewSizeChanged(viewSize: FloatVector2, rotation: Int) {
        Log.i("ParticleLifeViewModel::onViewSizeChanged")
        when (rotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 -> {
                parameters.value.runtime.xMax = viewSize.x.toDouble()
                parameters.value.runtime.yMax = viewSize.y.toDouble()
            }
            else -> {
                parameters.value.runtime.xMax = viewSize.y.toDouble()
                parameters.value.runtime.yMax = viewSize.x.toDouble()
            }
        }
        renderer.screenRotation = rotation
    }

    fun changeRuntimeParameters(block: ParticleLifeParameters.RuntimeParameters.() -> Unit) {
        updateParameters { runtime.block() }
    }

    fun changeGenerationParameters(block: ParticleLifeParameters.GenerationParameters.() -> Unit) {
        updateParameters { generation.block() }
    }

    fun generateNewParticles() {
        Log.i("ParticleLifeViewModel::generateNewParticles")
        val newParameters = with(parameters.value) {
            val species = generation.generateSpecies()
            val forceStrengths = generation.generateRandomForceStrengthMatrix()
            val (forceDistanceLowerBounds, forceDistanceUpperBounds) = generation.generateRandomForceDistanceMatrices()
            ParticleLifeParameters(
                generation = generation.copy(),
                runtime = runtime.copy(
                    forceStrengths = forceStrengths,
                    forceDistanceLowerBounds = forceDistanceLowerBounds,
                    forceDistanceUpperBounds = forceDistanceUpperBounds
                ),
                species = species
            )
        }
        animation.restart(newParameters)
        editForceStrengthsSelectedSpeciesIndex.value = 0
        editForceDistancesSelectedSpeciesIndex.value = 0
        parameters.value = newParameters
    }

    fun changeWallpaperParameters(block: ParticleLifeParameters.() -> Unit?) {
        wallpaperParameters.value = wallpaperParameters.value.also {
            prefs.wallpaperRandomise = it.block() == null
            prefs.setWallpaperParameters(it)
            prefs.wallpaperParametersChanged = true
        }
    }

    fun importWallpaperSettings() {
        Log.i("ParticleLifeViewModel::importWallpaperSettings")
        selectedWallpaperPhysics.value = ParticleLifeParameters.RuntimeParameters.Preset.Custom
        changeWallpaperShuffleForceValues(ParticleLifeParameters.ShuffleForceValues.Never)
        prefs.wallpaperParametersChanged = true
        // TODO
    }

    fun changeWallpaperShuffleForceValues(value: ParticleLifeParameters.ShuffleForceValues) {
        Log.i("ParticleLifeViewModel::changeWallpaperShuffleForceValues")
        wallpaperShuffleForceValues.value = value
        prefs.wallpaperShuffleForceValues = value
    }

    override fun onCleared() {
        super.onCleared()
        animationRunner.stop()
    }
}
