package com.ridwanstandingby.particlelife.domain

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.Surface
import com.ridwanstandingby.verve.animation.AnimationRenderer
import com.ridwanstandingby.verve.math.IntVector2

class ParticleLifeRenderer(var screenRotation: Int = Surface.ROTATION_0) : AnimationRenderer() {

    var getParticles: (() -> List<Particle>)? = null
    var getSpecies: (() -> List<Species>)? = null
    var getBounds: (() -> IntVector2)? = null

    override fun updateCanvas(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)

        val species = getSpecies?.invoke()
        val (width, height) = getBounds?.invoke() ?: IntVector2(canvas.width, canvas.height)

        getParticles?.invoke()?.forEach {
            canvas.drawCircle(
                transformX(it.x, it.y, width, height),
                transformY(it.x, it.y, width, height),
                PARTICLE_RADIUS,
                species?.get(it.speciesIndex)?.paint ?: Paint()
            )
        }
    }

    private fun transformX(x: Double, y: Double, width: Int, height: Int): Float =
        when (screenRotation) {
            Surface.ROTATION_90 -> y
            Surface.ROTATION_180 -> height - x
            Surface.ROTATION_270 -> width - y
            else -> x
        }.toFloat()

    private fun transformY(x: Double, y: Double, width: Int, height: Int): Float =
        when (screenRotation) {
            Surface.ROTATION_90 -> height - x
            Surface.ROTATION_180 -> width - y
            Surface.ROTATION_270 -> x
            else -> y
        }.toFloat()

    fun inverseTransformX(x: Float, y: Float, width: Int, height: Int): Double =
        when (screenRotation) {
            Surface.ROTATION_90 -> width - y
            Surface.ROTATION_180 -> height - x
            Surface.ROTATION_270 -> y
            else -> x
        }.toDouble()

    fun inverseTransformY(x: Float, y: Float, width: Int, height: Int): Double =
        when (screenRotation) {
            Surface.ROTATION_90 -> x
            Surface.ROTATION_180 -> width - y
            Surface.ROTATION_270 -> height - x
            else -> y
        }.toDouble()

    fun inverseTransformDX(x: Float, y: Float): Double =
        when (screenRotation) {
            Surface.ROTATION_90 -> -y
            Surface.ROTATION_180 -> -x
            Surface.ROTATION_270 -> y
            else -> x
        }.toDouble()

    fun inverseTransformDY(x: Float, y: Float): Double =
        when (screenRotation) {
            Surface.ROTATION_90 -> x
            Surface.ROTATION_180 -> -y
            Surface.ROTATION_270 -> -x
            else -> y
        }.toDouble()

    companion object {
        private const val PARTICLE_RADIUS = 7f
    }
}
