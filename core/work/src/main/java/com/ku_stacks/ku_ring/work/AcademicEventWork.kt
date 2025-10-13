package com.ku_stacks.ku_ring.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.ku_stacks.ku_ring.domain.academicevent.repository.AcademicEventRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class AcademicEventWork @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val academicEventRepository: AcademicEventRepository
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        Timber.tag(WORK_NAME).d("AcademicEventWork Started")
        return academicEventRepository.fetchAcademicEventsFromRemote()
            .fold(
                onSuccess = {
                    Result.success()
                },
                onFailure = {
                    Timber.e(it)
                    Result.failure()
                },
            )
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return super.getForegroundInfo()
    }

    companion object {
        const val WORK_NAME = "AcademicEventSync"
    }
}