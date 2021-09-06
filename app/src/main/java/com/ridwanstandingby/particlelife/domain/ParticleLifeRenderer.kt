package com.ridwanstandingby.particlelife.domain

import android.graphics.Canvas
import android.graphics.Color
import android.view.Surface
import com.ridwanstandingby.verve.animation.AnimationRenderer

class ParticleLifeRenderer(var screenRotation: Int = Surface.ROTATION_0) : AnimationRenderer() {

    var getParticles: (() -> List<Particle>)? = null

    override fun updateCanvas(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)

        getParticles?.invoke()?.forEach {
            canvas.drawCircle(
                canvas.correctRotationX(it.x, it.y),
                canvas.correctRotationY(it.x, it.y),
                PARTICLE_RADIUS,
                it.species.paint
            )
        }
    }

    private fun Canvas.correctRotationX(x: Double, y: Double): Float =
        when (screenRotation) {
            Surface.ROTATION_90 -> y
            Surface.ROTATION_180 -> height - x
            Surface.ROTATION_270 -> width - y
            else -> x
        }.toFloat()

    private fun Canvas.correctRotationY(x: Double, y: Double): Float =
        when (screenRotation) {
            Surface.ROTATION_90 -> height - x
            Surface.ROTATION_180 -> width - y
            Surface.ROTATION_270 -> x
            else -> y
        }.toFloat()

    companion object {
        private const val PARTICLE_RADIUS = 7f
    }
}
