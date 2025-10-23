package com.ku_stacks.ku_ring.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ku_stacks.ku_ring.notice.repository.NoticeRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class NoticeCacheClearWork @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val noticeRepository: NoticeRepository,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return try {
            noticeRepository.deleteAllNoticeRecord()
            Result.success()
        } catch (e: Exception) {
            Timber.e(e)
            Result.failure()
        }
    }

    companion object {
        const val WORK_NAME = "NoticeCacheCleaner"
    }
}