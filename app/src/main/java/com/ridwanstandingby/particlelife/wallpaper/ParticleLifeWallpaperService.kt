package com.ridwanstandingby.particlelife.wallpaper

import android.content.res.Resources
import android.service.wallpaper.WallpaperService
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.ridwanstandingby.particlelife.data.prefs.PreferencesManager
import com.ridwanstandingby.particlelife.domain.ParticleLifeAnimation
import com.ridwanstandingby.particlelife.domain.ParticleLifeInput
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters
import com.ridwanstandingby.particlelife.domain.ParticleLifeRenderer
import com.ridwanstandingby.particlelife.logging.Log
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

        private val swipeDetector = SwipeDetector()
        private val pressDetector = PressDetector()
        private val motionEventHandlers: MutableSet<(MotionEvent) -> Unit> = mutableSetOf()

        private var latestWidth: Double? = null
        private var latestHeight: Double? = null
        private var lastRandomiseUnixMs: Long = 0

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
            Log.i("ParticleLifeWallpaperService::onCreate")

            surfaceHolder?.let { animationRunner.attachSurfaceHolder(it) }

            animationRunner.start(
                ParticleLifeAnimation(
                    getParameters(randomiseMatrices = false),
                    ParticleLifeRenderer(),
                    ParticleLifeInput(swipeDetector, pressDetector)
                ).also { animation = it }
            )

            configureHandOfGod()
        }

        private fun getParameters(randomiseMatrices: Boolean) =
            (prefs.getWallpaperParameters(randomiseMatrices = randomiseMatrices)
                ?: ParticleLifeParameters.buildDefault(
                    0.0, 0.0,
                    ParticleLifeParameters.GenerationParameters()
                )).apply {
                runtime.xMax =
                    latestWidth ?: Resources.getSystem().displayMetrics.widthPixels.toDouble()
                runtime.yMax =
                    latestHeight ?: Resources.getSystem().displayMetrics.heightPixels.toDouble()
                if (prefs.wallpaperMode == WallpaperMode.Randomise) runtime.randomise()
            }

        private fun configureHandOfGod() {
            if (animation.parameters.runtime.handOfGodEnabled) {
                setTouchEventsEnabled(true)
                if (animation.parameters.runtime.herdEnabled) {
                    motionEventHandlers.add(swipeDetector::handleMotionEvent)
                } else {
                    motionEventHandlers.remove(swipeDetector::handleMotionEvent)
                }
                if (animation.parameters.runtime.beckonEnabled) {
                    motionEventHandlers.add(pressDetector::handleMotionEvent)
                } else {
                    motionEventHandlers.remove(pressDetector::handleMotionEvent)
                }
            } else {
                setTouchEventsEnabled(false)
                motionEventHandlers.clear()
            }
        }

        override fun onDesiredSizeChanged(desiredWidth: Int, desiredHeight: Int) {
            super.onDesiredSizeChanged(desiredWidth, desiredHeight)
            Log.i("ParticleLifeWallpaperService::onDesiredSizeChanged")
            animation.parameters.runtime.xMax = desiredWidth.toDouble().also { latestWidth = it }
            animation.parameters.runtime.yMax = desiredHeight.toDouble().also { latestHeight = it }
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            Log.i("ParticleLifeWallpaperService::onVisibilityChanged")
            if (visible) {
                reloadParametersIfNecessary()
                randomiseParametersIfNecessary()
                animationRunner.resume()
            } else {
                animationRunner.pause()
            }
        }

        private fun reloadParametersIfNecessary() {
            if (prefs.wallpaperParametersChanged) {
                prefs.wallpaperParametersChanged = false
                Log.i("ParticleLifeWallpaperService: Reloading wallpaper parameters")
                val newParams = getParameters(randomiseMatrices = false)
                if (newParams.generation != animation.parameters.generation) {
                    animation.restart(newParams)
                } else {
                    animation.parameters.runtime = newParams.runtime
                }
                configureHandOfGod()
            }
        }

        private fun randomiseParametersIfNecessary() {
            val now = System.currentTimeMillis()
            val doRandomise = when (val shuffle = prefs.wallpaperShuffleForceValues) {
                ParticleLifeParameters.ShuffleForceValues.Always -> true
                is ParticleLifeParameters.ShuffleForceValues.Timed ->
                    now > lastRandomiseUnixMs + shuffle.time.inWholeMilliseconds
                else -> false
            }

            if (doRandomise) {
                animation.parameters.runtime = getParameters(randomiseMatrices = true).runtime
                lastRandomiseUnixMs = now
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            animationRunner.stop()
        }

        override fun onTouchEvent(event: MotionEvent?) {
            super.onTouchEvent(event)
            if (event != null) {
                if (motionEventHandlers.isEmpty()) return super.onTouchEvent(event)
                this.motionEventHandlers.forEach { it.invoke(event) }
            }
        }
    }
}