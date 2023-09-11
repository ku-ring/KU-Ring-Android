package com.ku_stacks.ku_ring

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class KuRingApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setMinimumLoggingLevel(Log.INFO)
        .setWorkerFactory(workerFactory)
        .build()
}