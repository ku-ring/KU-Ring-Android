package com.ku_stacks.ku_ring.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.ku_stacks.ku_ring.domain.academicevent.repository.AcademicEventRepository
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.util.now
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import timber.log.Timber

@HiltWorker
class AcademicEventWork @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val academicEventRepository: AcademicEventRepository,
    private val pref: PreferenceUtil,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val lastDateAcademicEventShown = pref.lastDateAcademicEventShown
            .ifBlank { getFirstDayOfThreeYearsAgo().toString() }
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

    private fun getFirstDayOfThreeYearsAgo(): LocalDate {
        val targetYear = LocalDate.now().minus(3, DateTimeUnit.DAY)
        return LocalDate(targetYear.year, 1, 1)
    }

    companion object {
        const val WORK_NAME = "AcademicEventSync"
    }
}