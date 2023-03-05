package com.ku_stacks.ku_ring.data.api

import com.ku_stacks.ku_ring.data.api.request.FeedbackRequest
import com.ku_stacks.ku_ring.data.api.response.DefaultResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class FeedbackClient @Inject constructor(
    private val feedbackService: FeedbackService
) {
    fun sendFeedback(
        feedbackRequest: FeedbackRequest
    ): Single<DefaultResponse> = feedbackService.sendFeedback(feedbackRequest)
}