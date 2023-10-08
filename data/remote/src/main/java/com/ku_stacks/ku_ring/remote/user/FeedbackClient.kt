package com.ku_stacks.ku_ring.remote.user

import com.ku_stacks.ku_ring.remote.user.request.FeedbackRequest
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class FeedbackClient @Inject constructor(
    private val feedbackService: FeedbackService
) {
    fun sendFeedback(
        token: String,
        feedbackRequest: FeedbackRequest,
    ): Single<DefaultResponse> {
        return feedbackService.sendFeedback(
            token = token,
            feedbackRequest = feedbackRequest
        )
    }
}