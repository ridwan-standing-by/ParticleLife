package com.ridwanstandingby.particlelife.ui

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.ridwanstandingby.particlelife.domain.ParticleLifeAnimation
import com.ridwanstandingby.particlelife.domain.ParticleLifeInput
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters
import com.ridwanstandingby.particlelife.domain.ParticleLifeRenderer
import com.ridwanstandingby.verve.animation.AnimationRunner
import com.ridwanstandingby.verve.math.FloatVector2

class ParticleLifeViewModel(val animationRunner: AnimationRunner) : ViewModel() {

    val parameters = ParticleLifeParameters.buildDefault(
        Resources.getSystem().displayMetrics.widthPixels.toDouble(),
        Resources.getSystem().displayMetrics.heightPixels.toDouble()
    )

    val renderer = ParticleLifeRenderer()

    val input = ParticleLifeInput()

    fun start() {
        animationRunner.start(ParticleLifeAnimation(parameters, renderer, input))
    }

    fun onViewSizeChanged(viewSize: FloatVector2) {
        parameters.runtime.xMax = viewSize.x.toDouble()
        parameters.runtime.yMax = viewSize.y.toDouble()
    }

    override fun onCleared() {
        super.onCleared()
        animationRunner.stop()
    }
}
