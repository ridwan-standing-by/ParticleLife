package com.ridwanstandingby.particlelife.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import com.ridwanstandingby.verve.activities.AnimationActivity
import com.ridwanstandingby.verve.activities.createSwipeDetector
import com.ridwanstandingby.verve.animation.AnimationRunner
import org.koin.androidx.viewmodel.ext.android.viewModel

class ParticleLifeActivity : AnimationActivity() {

    private val vm by viewModel<ParticleLifeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParticleLifeActivityUi(::createAndAttachAnimationView, ::onTouchEvent, vm)
        }
        vm.start(createSwipeDetector())
    }

    override fun getAnimationRunner(): AnimationRunner = vm.animationRunner
}
