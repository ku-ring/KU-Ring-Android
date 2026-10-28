package com.ku_stacks.ku_ring

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.ku_stacks.ku_ring.firebase.analytics.EventAnalytics
import com.ku_stacks.ku_ring.work.scheduler.ApplicationWork
import com.ku_stacks.ku_ring.work.scheduler.WorkScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class KuRingApplication : Application(), Configuration.Provider {
    // Application 주입 시 SDK를 생성해 첫 Activity보다 먼저 초기화한다.
    @Inject
    lateinit var eventAnalytics: EventAnalytics

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    @ApplicationWork
    lateinit var applicationWorkScheduler: WorkScheduler

    override val workManagerConfiguration: Configuration
        get() = Configuration
            .Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        applicationWorkScheduler.schedule()
    }
}
