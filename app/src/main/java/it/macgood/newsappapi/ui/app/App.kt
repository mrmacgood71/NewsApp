package it.macgood.newsappapi.ui.app

import android.app.Application
import it.macgood.newsappapi.BuildConfig
import it.macgood.newsappapi.ui.di.appModule
import it.macgood.newsappapi.ui.di.dataModule
import it.macgood.newsappapi.ui.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(appModule, networkModule, dataModule))
        }
    }

}