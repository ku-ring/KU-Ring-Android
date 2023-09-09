package com.ku_stacks.ku_ring

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.ku_stacks.ku_ring.navigator.KuringNavigator
import com.ku_stacks.ku_ring.work.ReEngagementNotificationWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class KuRingApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var navigator: KuringNavigator
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .setWorkerFactory(ReEngagementNotificationWorkerFactory(navigator::createMainIntent))
            .build()
    }
}