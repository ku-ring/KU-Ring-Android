package com.ku_stacks.ku_ring.remote.user

import com.ku_stacks.ku_ring.remote.user.request.AcademicEventNotificationRequest
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
    suspend fun setAcademicEventNotification(
        request: AcademicEventNotificationRequest,
    ): DefaultResponse<Nothing> = userService.patchAcademicEventNotification(request)

    suspend fun sendFeedback(
        token: String,
        feedbackRequest: FeedbackRequest,
    ): DefaultResponse<Nothing> {
        return userService.sendFeedback(
            token = token,
            feedbackRequest = feedbackRequest
        )
    }

    suspend fun registerUser(token: String): DefaultResponse<Nothing> =
        userService.registerUser(RegisterUserRequest(token))

    suspend fun getKuringBotQueryCount(token: String): Int {
        return suspendRunCatching {
            userService.getKuringBotQueryCount(token).data.leftAskCount
        }.getOrElse { 0 }
    }

    suspend fun getUserData(): UserDataResponse =
        userService.getUserData()

    suspend fun signUp(token: String, request: AuthorizeUserRequest): DefaultResponse<Nothing> =
        userService.signUp(
            token = token,
            request = request
        )

    suspend fun signIn(token: String, request: AuthorizeUserRequest): SignInResponse =
        userService.signIn(
            token = token,
            request = request
        )

    suspend fun logout(): DefaultResponse<Nothing> =
        userService.logout()

    suspend fun patchPassword(request: AuthorizeUserRequest): DefaultResponse<Nothing> =
        userService.patchPassword(
            request = request,
        )

    suspend fun withdrawUser(): DefaultResponse<Nothing> = userService.withdraw()
}