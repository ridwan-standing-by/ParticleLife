package com.ridwanstandingby.particlelife.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.ridwanstandingby.verve.animation.AnimationView

@Composable
fun ParticleLifeActivityUi(createAnimationView: () -> AnimationView) {
    ParticleLifeUi(createAnimationView)
}

@Composable
fun ParticleLifeUi(createAnimationView: () -> AnimationView) {
    MaterialTheme {
        Scaffold {
            AndroidView(modifier = Modifier.fillMaxSize(), factory = {
                createAnimationView()
            })
        }
    }
}
