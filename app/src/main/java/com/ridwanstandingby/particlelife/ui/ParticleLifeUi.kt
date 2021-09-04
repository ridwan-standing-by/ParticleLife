package com.ridwanstandingby.particlelife.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.AndroidView
import com.ridwanstandingby.verve.animation.AnimationView
import com.ridwanstandingby.verve.math.IntVector2

@Composable
fun ParticleLifeActivityUi(createAnimationView: (IntVector2) -> AnimationView) {
    ParticleLifeUi(createAnimationView)
}

@Composable
fun ParticleLifeUi(createAnimationView: (IntVector2) -> AnimationView) {
    MaterialTheme {
        Scaffold {
            Column(Modifier.fillMaxSize()) {
                BoxWithConstraints(
                    Modifier
                        .weight(0.9f)
                        .fillMaxSize()
                ) {
                    with(LocalDensity.current) {
                        AndroidView(modifier = Modifier.size(
                            width = maxWidth,
                            height = maxHeight,
                        ), factory = {
                            createAnimationView(
                                IntVector2(maxWidth.toPx().toInt(), maxHeight.toPx().toInt())
                            )
                        })
                    }
                }
//                Box(
//                    Modifier
//                        .weight(0.1f)
//                        .fillMaxSize()
//                ) {
//                }
            }

        }
    }
}
