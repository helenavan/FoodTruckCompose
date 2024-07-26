package com.toulousehvl.myfoodtruck

import android.app.Application
import com.toulousehvl.myfoodtruck.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Log Koin into Android logger
            // androidLogger()
            // Reference Android context
             androidContext(this@MainApplication)
            // Load modules
            modules(AppModule.appModule)
        }
    }
}