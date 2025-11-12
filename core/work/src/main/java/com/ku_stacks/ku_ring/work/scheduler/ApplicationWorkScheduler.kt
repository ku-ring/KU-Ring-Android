package com.ku_stacks.ku_ring.work.scheduler

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.ku_stacks.ku_ring.work.AcademicEventWork
import com.ku_stacks.ku_ring.work.NoticeCacheClearWork
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ApplicationWorkScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) : WorkScheduler {
    private val workManager = WorkManager.getInstance(context)

    override fun schedule() {
        enqueueAcademicEventWork()
        enqueueNoticeCacheClearWork()
    }

    private fun enqueueAcademicEventWork() {
        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<AcademicEventWork>()
            .build()

        workManager.enqueueUniqueWork(
            uniqueWorkName = AcademicEventWork.WORK_NAME,
            existingWorkPolicy = ExistingWorkPolicy.KEEP,
            request = oneTimeWorkRequest,
        )
    }

    private fun enqueueNoticeCacheClearWork() {
        // 하루 주기로 캐시된 공지를 삭제
        val clearCacheWorkRequest = PeriodicWorkRequestBuilder<NoticeCacheClearWork>(
            repeatInterval = 24,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        ).build()

        workManager.enqueueUniquePeriodicWork(
            NoticeCacheClearWork.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            clearCacheWorkRequest
        )
    }
}