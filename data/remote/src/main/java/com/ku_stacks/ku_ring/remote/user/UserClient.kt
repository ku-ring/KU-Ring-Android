package com.ku_stacks.ku_ring.remote.user

import com.ku_stacks.ku_ring.remote.user.request.FeedbackRequest
import com.ku_stacks.ku_ring.remote.user.request.RegisterUserRequest
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import javax.inject.Inject

class UserClient @Inject constructor(
    private val userService: UserService
) {
    suspend fun sendFeedback(
        token: String,
        feedbackRequest: FeedbackRequest,
    ): DefaultResponse {
        return userService.sendFeedback(
            token = token,
            feedbackRequest = feedbackRequest
        )
    }

    suspend fun registerUser(token: String): DefaultResponse =
        userService.registerUser(RegisterUserRequest(token))

    suspend fun getKuringBotQueryCount(token: String): Int {
        return try {
            userService.getKuringBotQueryCount(token).data.leftAskCount
        } catch (e: Exception) {
            0
        }
    }
}