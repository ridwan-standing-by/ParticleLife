package com.ridwanstandingby.particlelife.wallpaper

import android.content.res.Resources
import android.service.wallpaper.WallpaperService
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.ridwanstandingby.particlelife.data.PreferencesManager
import com.ridwanstandingby.particlelife.domain.ParticleLifeAnimation
import com.ridwanstandingby.particlelife.domain.ParticleLifeInput
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters
import com.ridwanstandingby.particlelife.domain.ParticleLifeRenderer
import com.ridwanstandingby.verve.animation.AnimationRunner
import com.ridwanstandingby.verve.sensor.press.PressDetector
import com.ridwanstandingby.verve.sensor.swipe.SwipeDetector
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent

class ParticleLifeWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine = ParticleLifeWallpaperEngine()

    inner class ParticleLifeWallpaperEngine : Engine(), KoinComponent {

        private val animationRunner by inject<AnimationRunner>()
        private val prefs by inject<PreferencesManager>()

        private lateinit var animation: ParticleLifeAnimation

        private var latestWidth: Double? = null
        private var latestHeight: Double? = null

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)

            surfaceHolder?.let { animationRunner.attachSurfaceHolder(it) }

            animationRunner.start(
                ParticleLifeAnimation(
                    getParameters(),
                    ParticleLifeRenderer(),
                    ParticleLifeInput()
                ).also { animation = it }
            )

            configureHandOfGod()
        }

        private fun getParameters() =
            (prefs.wallpaperParameters ?: ParticleLifeParameters.buildDefault(
                0.0, 0.0,
                ParticleLifeParameters.GenerationParameters()
            )).apply {
                runtime.xMax =
                    latestWidth ?: Resources.getSystem().displayMetrics.widthPixels.toDouble()
                runtime.yMax =
                    latestHeight ?: Resources.getSystem().displayMetrics.heightPixels.toDouble()
                if (prefs.wallpaperRandomise) runtime.randomise()
            }

        private fun configureHandOfGod() {
            if (animation.parameters.runtime.handOfGodEnabled) {
                setTouchEventsEnabled(true)
                if (animation.parameters.runtime.herdEnabled) {
                    SwipeDetector().also {
                        animation.input.swipeDetector = it
                        motionEventHandlers.add(it::handleMotionEvent)
                    }
                }
                if (animation.parameters.runtime.beckonEnabled) {
                    PressDetector().also {
                        animation.input.pressDetector = it
                        motionEventHandlers.add(it::handleMotionEvent)
                    }
                }
            }
        }

        override fun onDesiredSizeChanged(desiredWidth: Int, desiredHeight: Int) {
            super.onDesiredSizeChanged(desiredWidth, desiredHeight)
            animation.parameters.runtime.xMax = desiredWidth.toDouble().also { latestWidth = it }
            animation.parameters.runtime.yMax = desiredHeight.toDouble().also { latestHeight = it }
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            if (visible) {
                animationRunner.resume()
                animation.parameters.runtime = getParameters().runtime
            } else {
                animationRunner.pause()
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            animationRunner.stop()
        }

        private val motionEventHandlers: MutableList<(MotionEvent) -> Unit> = mutableListOf()
        override fun onTouchEvent(event: MotionEvent?) {
            super.onTouchEvent(event)
            if (event != null) {
                if (motionEventHandlers.isEmpty()) return super.onTouchEvent(event)
                this.motionEventHandlers.forEach { it.invoke(event) }
            }
        }
    }
}