package com.ridwanstandingby.particlelife.ui

import android.content.res.Resources
import android.view.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.lifecycle.ViewModel
import com.ridwanstandingby.particlelife.domain.ParticleLifeAnimation
import com.ridwanstandingby.particlelife.domain.ParticleLifeInput
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters
import com.ridwanstandingby.particlelife.domain.ParticleLifeRenderer
import com.ridwanstandingby.verve.animation.AnimationRunner
import com.ridwanstandingby.verve.math.FloatVector2

class ParticleLifeViewModel(val animationRunner: AnimationRunner) : ViewModel() {

    val parameters = mutableStateOf(
        ParticleLifeParameters.buildDefault(
            Resources.getSystem().displayMetrics.widthPixels.toDouble(),
            Resources.getSystem().displayMetrics.heightPixels.toDouble()
        ), neverEqualPolicy()
    )

    private fun updateParameters(block: ParticleLifeParameters.() -> Unit) {
        parameters.value = parameters.value.apply(block)
    }

    val preGenSpecies = mutableStateOf(parameters.value.species)
    val preGenMatrix = mutableStateOf(parameters.value.runtime.interactionMatrix)

    private val renderer = ParticleLifeRenderer()

    private val input = ParticleLifeInput()

    fun start() {
        animationRunner.start(ParticleLifeAnimation(parameters.value, renderer, input))
    }

    fun onViewSizeChanged(viewSize: FloatVector2, rotation: Int) {
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

    fun onNumberOfSpeciesChanged(nSpeciesNew: Int) {
        val oldSpecies = preGenSpecies.value.toList()

        updateParameters { generation.nSpecies = nSpeciesNew }
        preGenSpecies.value = parameters.value.generation.generateRandomSpecies()

        val newSpecies = preGenSpecies.value
        val oldMatrix = preGenMatrix.value
        val newMatrix = oldMatrix.map { it.toMutableList() }.toMutableList()
        val nSpeciesOld = oldMatrix.size

        with(parameters.value.generation) {
            if (nSpeciesNew < nSpeciesOld) {
                (nSpeciesNew until nSpeciesOld).reversed().forEach { i ->
                    newMatrix.removeAt(i)
                    (0..nSpeciesNew).forEach { j ->
                        newMatrix[j].removeAt(i)
                    }
                }
            } else if (nSpeciesNew > nSpeciesOld) {
                (nSpeciesOld until nSpeciesNew).forEach { i ->
                    newMatrix[i] = generateRandomInteractionVector().toMutableList()
                    (0..nSpeciesOld).forEach { j ->
                        newMatrix[j][i] = generateRandomInteractionValue()
                    }
                }
            }
        }
    }

    fun generateNewParticles() {
        val species = parameters.value.generation.generateRandomSpecies()
//        val particles = parameters.value.generation.generateRandomParticles()

    }

    override fun onCleared() {
        super.onCleared()
        animationRunner.stop()
    }
}
