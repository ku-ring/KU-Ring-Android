package com.ku_stacks.ku_ring.domain.user.repository

import com.ku_stacks.ku_ring.domain.CategoryOrder
import com.ku_stacks.ku_ring.domain.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun blockUser(
        userId: String,
        nickname: String
    )

    fun getBlackUserList(): Flow<List<String>>
    suspend fun sendFeedback(feedback: String): Result<Pair<Boolean, String>>
    suspend fun registerUser(token: String)

    suspend fun getUserData(): Result<User>
    suspend fun signUpUser(email: String, password: String): Result<Unit>
    suspend fun signInUser(email: String, password: String): Result<Unit>
    suspend fun logoutUser(): Result<Unit>
    suspend fun withdrawUser(): Result<Unit>
    suspend fun patchPassword(email: String, password: String): Result<Unit>

    suspend fun getCategoryOrders(): List<CategoryOrder>
    fun getCategoryOrdersAsFlow(): Flow<List<CategoryOrder>>
    suspend fun updateCategoryOrder(order: CategoryOrder)
    suspend fun updateCategoryOrders(orders: List<CategoryOrder>)
}