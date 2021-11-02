package com.ku_stacks.ku_ring.data.api

import com.ku_stacks.ku_ring.data.api.response.DefaultResponse
import com.ku_stacks.ku_ring.data.entity.Feedback
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface FeedbackService {
    @POST("feedback")
    fun sendFeedback(
        @Body feedback: Feedback
    ): Single<DefaultResponse>
}