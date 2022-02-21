package com.ridwanstandingby.particlelife

import android.app.Application
import com.ridwanstandingby.particlelife.di.KoinInjector
import com.ridwanstandingby.particlelife.logging.Log
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@Suppress("unused")
class ParticleLifeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
        Log.init(this)
    }

    private fun setupKoin() {
        startKoin {
            allowOverride(true)
//            androidLogger()
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE) // https://github.com/InsertKoinIO/koin/issues/1188
            androidContext(this@ParticleLifeApplication)
            modules(KoinInjector.buildModule())
        }
    }
}
