package com.ridwanstandingby.particlelife.domain

import android.graphics.*
import android.view.Surface
import com.ridwanstandingby.verve.animation.AnimationRenderer

class ParticleLifeRenderer(
    var screenRotation: Int = Surface.ROTATION_0,
    private val easterBitmap: Bitmap? = null
) : AnimationRenderer() {

    var getParticles: (() -> List<Particle>)? = null
    var getSpecies: (() -> List<Species>)? = null
    private var width: Int = 0
    private var height: Int = 0

    var easterEgg: Boolean = false

    override fun updateCanvas(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)

        val species = getSpecies?.invoke()
        width = canvas.width
        height = canvas.height

        if (easterEgg) {
            canvas.deployEasterEgg()
            return
        }

        getParticles?.invoke()?.forEach {
            canvas.drawCircle(
                transformX(it.x, it.y),
                transformY(it.x, it.y),
                PARTICLE_RADIUS,
                species?.get(it.speciesIndex)?.paint ?: Paint()
            )
        }
    }

    private fun transformX(x: Double, y: Double): Float =
        when (screenRotation) {
            Surface.ROTATION_90 -> y
            Surface.ROTATION_180 -> height - x
            Surface.ROTATION_270 -> width - y
            else -> x
        }.toFloat()

    private fun transformY(x: Double, y: Double): Float =
        when (screenRotation) {
            Surface.ROTATION_90 -> height - x
            Surface.ROTATION_180 -> width - y
            Surface.ROTATION_270 -> x
            else -> y
        }.toFloat()

    fun inverseTransformX(x: Float, y: Float): Double =
        when (screenRotation) {
            Surface.ROTATION_90 -> width - y
            Surface.ROTATION_180 -> height - x
            Surface.ROTATION_270 -> y
            else -> x
        }.toDouble()

    fun inverseTransformY(x: Float, y: Float): Double =
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

    private fun Canvas.deployEasterEgg() {
        if (easterBitmap == null) return
        getParticles?.invoke()?.forEach {
            val x = transformX(it.x, it.y)
            val y = transformY(it.x, it.y)
            drawBitmap(easterBitmap, null, RectF(x - 16f, y - 16f, x + 16f, y + 16f), null)
        }
    }

    companion object {
        private const val PARTICLE_RADIUS = 7f
    }
}
