package com.ku_stacks.ku_ring.data.api

import android.os.Build
import com.ku_stacks.ku_ring.BuildConfig
import com.ku_stacks.ku_ring.data.api.request.FeedbackRequest
import com.ku_stacks.ku_ring.data.api.response.DefaultResponse
import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import javax.inject.Inject

class FeedbackClient @Inject constructor(
    private val feedbackService: FeedbackService
) {
    fun sendFeedback(
        token: String,
        feedbackRequest: FeedbackRequest,
    ): Single<DefaultResponse> {
        val kuringVersion = BuildConfig.VERSION_NAME
        val kuringVersionHeader = "Kuring/$kuringVersion"

        val androidVersion = Build.VERSION.SDK_INT.toString() // 33
        val androidVersionHeader = "Android/$androidVersion"

        Timber.d("$androidVersionHeader, $kuringVersionHeader")
        return feedbackService.sendFeedback(
            token = token,
            appVersion = kuringVersionHeader,
            androidVersion = androidVersionHeader,
            feedbackRequest = feedbackRequest
        )
    }
}