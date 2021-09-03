package com.ridwanstandingby.particlelife.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import com.ridwanstandingby.particlelife.domain.ParticleLifeAnimation
import com.ridwanstandingby.particlelife.domain.ParticleLifeInput
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters
import com.ridwanstandingby.particlelife.domain.ParticleLifeRenderer
import com.ridwanstandingby.verve.activities.AnimationActivity
import com.ridwanstandingby.verve.animation.AnimationRule
import com.ridwanstandingby.verve.animation.AnimationView

class ParticleLifeActivity : AnimationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParticleLifeActivityUi(::createAnimationView)
        }
    }

    override fun defineAnimationView(): AnimationView = AnimationView(
        this,
        AnimationRule(
            ::ParticleLifeAnimation,
            ParticleLifeParameters(),
            ParticleLifeRenderer(),
            ParticleLifeInput()
        )
    )
}
