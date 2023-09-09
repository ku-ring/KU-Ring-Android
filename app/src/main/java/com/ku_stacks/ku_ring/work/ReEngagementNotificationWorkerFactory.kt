package com.ku_stacks.ku_ring.work

import android.content.Context
import android.content.Intent
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters

class ReEngagementNotificationWorkerFactory(private val createIntent: (Context) -> Intent) :
    WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return ReEngagementNotificationWork(appContext, workerParameters, createIntent)
    }
}