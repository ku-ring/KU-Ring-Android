package com.ku_stacks.ku_ring.user.repository

import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun blockUser(
        userId: String,
        nickname: String
    )

    fun getBlackUserList(): Flow<List<String>>
    suspend fun sendFeedback(feedback: String): Result<DefaultResponse>
    suspend fun registerUser(token: String): DefaultResponse

    suspend fun signUpUser(email: String, password: String): Result<Unit>
    suspend fun signInUser(email: String, password: String): Result<Unit>
    suspend fun logoutUser(): Result<Unit>
}