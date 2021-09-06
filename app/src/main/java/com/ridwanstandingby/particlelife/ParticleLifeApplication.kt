package com.ridwanstandingby.particlelife

import android.app.Application
import com.ridwanstandingby.particlelife.di.KoinInjector
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

@Suppress("unused")
class ParticleLifeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
    }

    private fun setupKoin() {
        startKoin {
            allowOverride(true)
            androidLogger()
            androidContext(this@ParticleLifeApplication)
            modules(KoinInjector.buildModule())
        }
    }
}
