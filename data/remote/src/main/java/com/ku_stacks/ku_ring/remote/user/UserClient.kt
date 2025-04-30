package com.ku_stacks.ku_ring.remote.user

import com.ku_stacks.ku_ring.remote.user.request.AuthorizeUserRequest
import com.ku_stacks.ku_ring.remote.user.request.FeedbackRequest
import com.ku_stacks.ku_ring.remote.user.request.RegisterUserRequest
import com.ku_stacks.ku_ring.remote.user.response.SignInResponse
import com.ku_stacks.ku_ring.remote.user.response.UserDataResponse
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import com.ku_stacks.ku_ring.util.suspendRunCatching
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
        return suspendRunCatching {
            userService.getKuringBotQueryCount(token).data.leftAskCount
        }.getOrElse { 0 }
    }

    suspend fun getUserData(accessToken: String): UserDataResponse =
        userService.getUserData(
            accessToken = accessToken
        )

    suspend fun signUp(token: String, request: AuthorizeUserRequest): DefaultResponse =
        userService.signUp(
            token = token,
            request = request
        )

    suspend fun signIn(token: String, request: AuthorizeUserRequest): SignInResponse =
        userService.signIn(
            token = token,
            request = request
        )

    suspend fun logout(token: String, accessToken: String): DefaultResponse =
        userService.logout(
            token = token,
            accessToken = accessToken
        )

    suspend fun patchPassword(request: AuthorizeUserRequest): DefaultResponse =
        userService.patchPassword(
            request = request,
        )

    suspend fun withdrawUser(accessToken: String): DefaultResponse =
        userService.withdraw(
            accessToken = accessToken
        )
}