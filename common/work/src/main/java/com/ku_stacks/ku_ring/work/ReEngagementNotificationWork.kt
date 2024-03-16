package com.ku_stacks.ku_ring.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ku_stacks.ku_ring.ui_util.KuringNavigator
import com.ku_stacks.ku_ring.util.KuringNotificationManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ReEngagementNotificationWork @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val navigator: KuringNavigator
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val intent = navigator.createMainIntent(applicationContext)
        KuringNotificationManager.showReengagementNotification(applicationContext, intent)
        return Result.success()
    }

    companion object {
        val WORK_NAME = "notification_work"
    }
}