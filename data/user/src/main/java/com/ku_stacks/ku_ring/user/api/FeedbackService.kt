package com.ku_stacks.ku_ring.user.api

import com.ku_stacks.ku_ring.user.api.request.FeedbackRequest
import com.ku_stacks.ku_ring.util.network.DefaultResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FeedbackService {
    @POST("v2/users/feedbacks")
    fun sendFeedback(
        @Header("User-Token") token: String,
        @Body feedbackRequest: FeedbackRequest,
    ): Single<DefaultResponse>
}