package com.ridwanstandingby.particlelife.di

import com.ridwanstandingby.particlelife.ui.ParticleLifeViewModel
import com.ridwanstandingby.verve.animation.AnimationRunner
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object KoinInjector {

    fun buildModule() =
        module {
            defineDomainComponents()
            defineUiComponents()
        }

    private fun Module.defineDomainComponents() {
        factory { AnimationRunner() }
    }

    private fun Module.defineUiComponents() {
        viewModel { ParticleLifeViewModel(get()) }
    }
}
