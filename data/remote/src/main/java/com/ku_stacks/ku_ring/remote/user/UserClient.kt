package com.ku_stacks.ku_ring.remote.user

import com.ku_stacks.ku_ring.remote.user.request.FeedbackRequest
import com.ku_stacks.ku_ring.remote.user.request.RegisterUserRequest
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class UserClient @Inject constructor(
    private val userService: UserService
) {
    fun sendFeedback(
        token: String,
        feedbackRequest: FeedbackRequest,
    ): Single<DefaultResponse> {
        return userService.sendFeedback(
            token = token,
            feedbackRequest = feedbackRequest
        )
    }

    suspend fun registerUser(token: String): DefaultResponse =
        userService.registerUser(RegisterUserRequest(token))
}