package com.tony.appbooster

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Application class that configures Hilt dependency injection and WorkManager
 * with HiltWorkerFactory so that Workers can receive injected dependencies.
 */
@HiltAndroidApp
class AppBooster : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
