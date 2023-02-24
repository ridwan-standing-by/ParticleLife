package com.ridwanstandingby.particlelife.ui

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import com.ridwanstandingby.particlelife.wallpaper.ParticleLifeWallpaperService
import com.ridwanstandingby.verve.activities.AnimationActivity
import com.ridwanstandingby.verve.activities.createPressDetector
import com.ridwanstandingby.verve.activities.createSwipeDetector
import com.ridwanstandingby.verve.animation.AnimationRunner
import org.koin.androidx.viewmodel.ext.android.viewModel


class ParticleLifeActivity : AnimationActivity() {

    private val vm by viewModel<ParticleLifeViewModel>()
    private val backHandler =
        ParticleLifeActivityBackHandler(this, { vm }, { onBackPressedDispatcher })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParticleLifeActivityUi(
                ::createAndAttachAnimationView,
                ::onTouchEvent,
                ::setWallpaperClicked,
                vm
            )
            backHandler.updateCallbackEnabledState()
        }
        vm.start(createSwipeDetector(), createPressDetector(), ::makeToast)
        backHandler.addCallbacks()
    }

    override fun getAnimationRunner(): AnimationRunner = vm.animationRunner

    private fun makeToast(toast: ToastMessage) {
        Toast.makeText(this, toast.id, Toast.LENGTH_LONG).show()
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
