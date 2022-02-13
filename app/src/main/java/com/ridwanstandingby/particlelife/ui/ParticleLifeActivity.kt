package com.ridwanstandingby.particlelife.ui

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import com.ridwanstandingby.particlelife.wallpaper.ParticleLifeWallpaperService
import com.ridwanstandingby.verve.activities.AnimationActivity
import com.ridwanstandingby.verve.activities.createPressDetector
import com.ridwanstandingby.verve.activities.createSwipeDetector
import com.ridwanstandingby.verve.animation.AnimationRunner
import org.koin.androidx.viewmodel.ext.android.viewModel


class ParticleLifeActivity : AnimationActivity() {

    private val vm by viewModel<ParticleLifeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParticleLifeActivityUi(
                ::createAndAttachAnimationView,
                ::onTouchEvent,
                ::setWallpaperClicked,
                vm
            )
        }
        vm.start(createSwipeDetector(), createPressDetector())
    }

    override fun getAnimationRunner(): AnimationRunner = vm.animationRunner

    override fun onBackPressed() =
        when {
            vm.editHandOfGodPanelExpanded.value ||
                    vm.editForceStrengthsPanelExpanded.value ||
                    vm.editForceDistancesPanelExpanded.value -> {
                vm.editHandOfGodPanelExpanded.value = false
                vm.editForceStrengthsPanelExpanded.value = false
                vm.editForceDistancesPanelExpanded.value = false
                vm.controlPanelExpanded.value = true
            }
            vm.controlPanelExpanded.value -> vm.controlPanelExpanded.value = false
            else -> super.onBackPressed()
        }

    private fun setWallpaperClicked() {
        val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
        intent.putExtra(
            WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
            ComponentName(this, ParticleLifeWallpaperService::class.java)
        )
        startActivity(intent)
    }
}
