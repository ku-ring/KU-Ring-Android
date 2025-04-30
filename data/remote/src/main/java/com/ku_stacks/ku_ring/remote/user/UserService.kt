package com.ku_stacks.ku_ring.remote.user

import com.ku_stacks.ku_ring.remote.user.request.AuthorizeUserRequest
import com.ku_stacks.ku_ring.remote.user.request.FeedbackRequest
import com.ku_stacks.ku_ring.remote.user.request.RegisterUserRequest
import com.ku_stacks.ku_ring.remote.user.response.KuringBotQueryCountResponse
import com.ku_stacks.ku_ring.remote.user.response.SignInResponse
import com.ku_stacks.ku_ring.remote.user.response.UserDataResponse
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
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

    @GET("v2/users/user-me")
    suspend fun getUserData(
        @Header("Authorization") accessToken: String,
    ): UserDataResponse

    @POST("v2/users/signup")
    suspend fun signUp(
        @Header("User-Token") token: String,
        @Body request: AuthorizeUserRequest,
    ): DefaultResponse

    @POST("v2/users/login")
    suspend fun signIn(
        @Header("User-Token") token: String,
        @Body request: AuthorizeUserRequest,
    ): SignInResponse

    @POST("v2/users/logout")
    suspend fun logout(
        @Header("User-Token") token: String,
        @Header("Authorization") accessToken: String,
    ): DefaultResponse

    @PATCH("v2/users/password")
    suspend fun patchPassword(
        @Body request: AuthorizeUserRequest,
    ): DefaultResponse

    @DELETE("v2/users/withdraw")
    suspend fun withdraw(
        @Header("Authorization") accessToken: String,
    ): DefaultResponse
}
