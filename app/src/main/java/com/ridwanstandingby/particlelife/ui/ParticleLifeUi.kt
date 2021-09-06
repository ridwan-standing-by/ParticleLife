package com.ridwanstandingby.particlelife.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.viewinterop.AndroidView
import com.ridwanstandingby.verve.animation.AnimationView
import com.ridwanstandingby.verve.math.FloatVector2

@Composable
fun ParticleLifeActivityUi(
    createAnimationView: () -> AnimationView,
    vm: ParticleLifeViewModel
) {
    ParticleLifeUi(createAnimationView, vm::onViewSizeChanged)
}

@Composable
fun ParticleLifeUi(createAnimationView: () -> AnimationView, onViewSizeChanged: (FloatVector2, Int) -> Unit) {
    MaterialTheme {
        Scaffold {
            Column(Modifier.fillMaxSize()) {
                BoxWithConstraints(
                    Modifier
                        .weight(0.9f)
                        .fillMaxSize()
                ) {
                    with(LocalDensity.current) {
                        val rotation = LocalView.current.display.rotation
                        AndroidView(modifier = Modifier.size(
                            width = maxWidth,
                            height = maxHeight,
                        ), factory = {
                            onViewSizeChanged(FloatVector2(maxWidth.toPx(), maxHeight.toPx()), rotation)
                            createAnimationView()
                        }, update = {
                            onViewSizeChanged(FloatVector2(maxWidth.toPx(), maxHeight.toPx()), rotation)
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
