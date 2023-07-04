package com.ku_stacks.ku_ring.data.api

import com.ku_stacks.ku_ring.data.api.request.FeedbackRequest
import com.ku_stacks.ku_ring.data.api.response.DefaultResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FeedbackService {
    @POST("v2/users/feedbacks")
    fun sendFeedback(
        @Header("User-Token") token: String,
        @Header("User-Agent") appVersion: String,
        @Header("User-Agent") androidVersion: String,
        @Body feedbackRequest: FeedbackRequest,
    ): Single<DefaultResponse>
}