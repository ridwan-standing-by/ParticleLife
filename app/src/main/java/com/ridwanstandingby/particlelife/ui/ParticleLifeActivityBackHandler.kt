package com.ridwanstandingby.particlelife.ui

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.lifecycle.LifecycleOwner

class ParticleLifeActivityBackHandler(
    private val lifecycleOwner: LifecycleOwner,
    private val vm: () -> ParticleLifeViewModel,
    private val onBackPressedDispatcher: () -> OnBackPressedDispatcher
) {

    private val controlPanelBackPressedCallback = onBackPressedCallback {
        vm().controlPanelExpanded.value = false
    }
    private val editHandOfGodPanelBackPressedCallback = onBackPressedCallback {
        vm().editHandOfGodPanelExpanded.value = HandOfGodPanelMode.OFF
        vm().controlPanelExpanded.value = true
    }
    private val editForceStrengthsPanelBackPressedCallback = onBackPressedCallback {
        vm().editForceStrengthsPanelExpanded.value = false
        vm().controlPanelExpanded.value = true
    }
    private val editForceDistancesPanelBackPressedCallback = onBackPressedCallback {
        vm().editForceDistancesPanelExpanded.value = false
        vm().controlPanelExpanded.value = true
    }

    fun updateCallbackEnabledState() {
        controlPanelBackPressedCallback.isEnabled =
            vm().controlPanelExpanded.value
        editHandOfGodPanelBackPressedCallback.isEnabled =
            vm().editHandOfGodPanelExpanded.value != HandOfGodPanelMode.OFF
        editForceStrengthsPanelBackPressedCallback.isEnabled =
            vm().editForceStrengthsPanelExpanded.value
        editForceDistancesPanelBackPressedCallback.isEnabled =
            vm().editForceDistancesPanelExpanded.value
    }

    fun addCallbacks() {
        onBackPressedDispatcher()
            .addCallback(lifecycleOwner, controlPanelBackPressedCallback)
        onBackPressedDispatcher()
            .addCallback(lifecycleOwner, editHandOfGodPanelBackPressedCallback)
        onBackPressedDispatcher()
            .addCallback(lifecycleOwner, editForceStrengthsPanelBackPressedCallback)
        onBackPressedDispatcher()
            .addCallback(lifecycleOwner, editForceDistancesPanelBackPressedCallback)
    }

    private fun onBackPressedCallback(onBackPressed: () -> Unit) =
        object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
}