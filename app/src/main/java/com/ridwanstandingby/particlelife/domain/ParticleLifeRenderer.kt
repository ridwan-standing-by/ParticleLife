package com.ridwanstandingby.particlelife.domain

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.ridwanstandingby.verve.animation.AnimationRenderer

class ParticleLifeRenderer : AnimationRenderer() {

    var getParticles: (() -> List<Particle>)? = null

    override fun updateCanvas(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)

        getParticles?.invoke()?.forEach {
            canvas.drawCircle(
                it.x.toFloat(),
                it.y.toFloat(),
                7f,
                Paint().apply { color = it.species.color })
        }
    }
}
