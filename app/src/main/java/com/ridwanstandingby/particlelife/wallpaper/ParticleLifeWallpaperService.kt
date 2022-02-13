package com.ridwanstandingby.particlelife.wallpaper

import android.content.res.Resources
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import com.ridwanstandingby.particlelife.data.PreferencesManager
import com.ridwanstandingby.particlelife.domain.ParticleLifeAnimation
import com.ridwanstandingby.particlelife.domain.ParticleLifeInput
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters
import com.ridwanstandingby.particlelife.domain.ParticleLifeRenderer
import com.ridwanstandingby.verve.animation.AnimationRunner
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent

class ParticleLifeWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine = ParticleLifeWallpaperEngine()

    inner class ParticleLifeWallpaperEngine : Engine(), KoinComponent {

        private val animationRunner by inject<AnimationRunner>()
        private val prefs by inject<PreferencesManager>()

        private val renderer = ParticleLifeRenderer()
        private val input = ParticleLifeInput()
        private lateinit var animation: ParticleLifeAnimation

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)

            surfaceHolder?.let { animationRunner.attach(it) }

            animationRunner.start(
                ParticleLifeAnimation(getParameters(), renderer, input).also { animation = it }
            )
        }

        private fun getParameters() =
            (prefs.wallpaperParameters ?: ParticleLifeParameters.buildDefault(
                0.0, 0.0,
                ParticleLifeParameters.GenerationParameters()
            )).apply {
                runtime.xMax = Resources.getSystem().displayMetrics.widthPixels.toDouble()
                runtime.yMax = Resources.getSystem().displayMetrics.heightPixels.toDouble()
            }


        override fun onDesiredSizeChanged(desiredWidth: Int, desiredHeight: Int) {
            super.onDesiredSizeChanged(desiredWidth, desiredHeight)
            animation.parameters.runtime.xMax = desiredWidth.toDouble()
            animation.parameters.runtime.yMax = desiredHeight.toDouble()
        }
    }
}