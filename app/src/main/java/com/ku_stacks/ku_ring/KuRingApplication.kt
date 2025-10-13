package com.ku_stacks.ku_ring

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.ku_stacks.ku_ring.work.AcademicEventWork
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class KuRingApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration
            .Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        enqueueAcademicEventWork()
    }

    private fun enqueueAcademicEventWork() {
        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<AcademicEventWork>()
            .build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            uniqueWorkName = AcademicEventWork.WORK_NAME,
            existingWorkPolicy = ExistingWorkPolicy.REPLACE,
            request = oneTimeWorkRequest,
        )
    }
}
