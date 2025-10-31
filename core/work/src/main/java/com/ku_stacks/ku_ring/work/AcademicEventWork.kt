package com.ku_stacks.ku_ring.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.ku_stacks.ku_ring.domain.academicevent.repository.AcademicEventRepository
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class AcademicEventWork @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val academicEventRepository: AcademicEventRepository,
    private val pref: PreferenceUtil,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        // lastDateAcademicEventShown이 비어있다면 null부터 null까지, 즉 모든 학사일정을 불러옵니다.
        val lastDateAcademicEventShown = pref.lastDateAcademicEventShown.ifBlank { null }
        return academicEventRepository.fetchAcademicEventsFromRemote(
            startDate = lastDateAcademicEventShown,
        ).fold(
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