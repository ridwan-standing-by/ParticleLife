package com.ridwanstandingby.particlelife.di

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.ridwanstandingby.particlelife.R
import com.ridwanstandingby.particlelife.data.PreferencesManager
import com.ridwanstandingby.particlelife.ui.ParticleLifeViewModel
import com.ridwanstandingby.verve.animation.AnimationRunner
import org.koin.android.ext.koin.androidContext
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
        single { PreferencesManager(androidContext()) }

        factory { AnimationRunner() }
        factory<Bitmap> { BitmapFactory.decodeResource(androidContext().resources, R.drawable.dan) }
    }

    private fun Module.defineUiComponents() {
        viewModel { ParticleLifeViewModel(get(), get(),get()) }
    }
}
