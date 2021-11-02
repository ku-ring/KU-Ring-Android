package com.ku_stacks.ku_ring.data.api

import com.ku_stacks.ku_ring.data.api.response.DefaultResponse
import com.ku_stacks.ku_ring.data.entity.Feedback
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class FeedbackClient @Inject constructor(
    private val feedbackService: FeedbackService
){
    fun sendFeedback(
        feedback: Feedback
    ): Single<DefaultResponse> = feedbackService.sendFeedback(feedback)
}