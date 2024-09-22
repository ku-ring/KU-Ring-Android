package com.ku_stacks.ku_ring.remote.user

import com.ku_stacks.ku_ring.remote.user.request.FeedbackRequest
import com.ku_stacks.ku_ring.remote.user.request.RegisterUserRequest
import com.ku_stacks.ku_ring.remote.user.response.KuringBotQueryCountResponse
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {
    @POST("v2/users/feedbacks")
    suspend fun sendFeedback(
        @Header("User-Token") token: String,
        @Body feedbackRequest: FeedbackRequest,
    ): DefaultResponse

    @POST("v2/users")
    suspend fun registerUser(@Body registerUserRequest: RegisterUserRequest): DefaultResponse

    @GET("v2/users/ask-counts")
    suspend fun getKuringBotQueryCount(@Header("User-Token") token: String): KuringBotQueryCountResponse
}