package com.kl3jvi.yonda.application

import android.app.Application
import com.kl3jvi.yonda.di.allModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class YondaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Using the log level as Level.DEBUG while using kotlin 1.6 crashes the app.
            // Using Level.ERROR is a workaround until it's fixed.
            androidLogger(Level.ERROR)
            androidContext(this@YondaApplication)
            modules(
                allModules,
            )
        }
    }
}
