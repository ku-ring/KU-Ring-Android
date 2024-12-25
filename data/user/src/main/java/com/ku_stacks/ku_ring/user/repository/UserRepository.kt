package com.ku_stacks.ku_ring.user.repository

import com.ku_stacks.ku_ring.domain.CategoryOrder
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

    suspend fun getCategoryOrders(): List<CategoryOrder>
    fun getCategoryOrdersAsFlow(): Flow<List<CategoryOrder>>
    suspend fun updateCategoryOrder(order: CategoryOrder)
    suspend fun updateCategoryOrders(orders: List<CategoryOrder>)
}