package com.ridwanstandingby.particlelife.domain

import android.graphics.Canvas
import android.graphics.Color
import com.ridwanstandingby.verve.animation.AnimationRenderer

class ParticleLifeRenderer() : AnimationRenderer() {

    var getParticles: (() -> List<Particle>)? = null

    override fun updateCanvas(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)

        getParticles?.invoke()?.forEach {
            canvas.drawCircle(
                it.x.toFloat(),
                it.y.toFloat(),
                PARTICLE_RADIUS,
                it.species.paint
            )
        }
    }

    companion object {
        private const val PARTICLE_RADIUS = 7f
    }
}
