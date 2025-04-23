package com.ku_stacks.ku_ring.user.repository

import com.ku_stacks.ku_ring.domain.CategoryOrder
import com.ku_stacks.ku_ring.domain.User
import com.ku_stacks.ku_ring.domain.user.repository.UserRepository
import com.ku_stacks.ku_ring.local.entity.BlackUserEntity
import com.ku_stacks.ku_ring.local.room.BlackUserDao
import com.ku_stacks.ku_ring.local.room.CategoryOrderDao
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.remote.user.UserClient
import com.ku_stacks.ku_ring.remote.user.request.AuthorizeUserRequest
import com.ku_stacks.ku_ring.remote.user.request.FeedbackRequest
import com.ku_stacks.ku_ring.user.mapper.toDomain
import com.ku_stacks.ku_ring.user.mapper.toEntity
import com.ku_stacks.ku_ring.util.suspendRunCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dao: BlackUserDao,
    private val userClient: UserClient,
    private val pref: PreferenceUtil,
    private val categoryOrderDao: CategoryOrderDao,
) : UserRepository {
    override suspend fun blockUser(
        userId: String,
        nickname: String
    ) {
        return dao.blockUser(
            BlackUserEntity(
                userId = userId,
                nickname = nickname,
                blockedAt = System.currentTimeMillis()
            )
        )
    }

    override fun getBlackUserList(): Flow<List<String>> {
        return dao.getBlackList().map { it.map { it.userId } }
    }

    override suspend fun sendFeedback(feedback: String): Result<Pair<Boolean, String>> {
        return suspendRunCatching {
            userClient.sendFeedback(
                token = pref.fcmToken,
                feedbackRequest = FeedbackRequest(feedback)
            )
        }.map {
            it.isSuccess to it.resultMsg
        }
    }

    override suspend fun registerUser(token: String) {
        userClient.registerUser(token)
    }

    override suspend fun getUserData(): Result<User> = runCatching {
        userClient.getUserData().toDomain()
    }

    override suspend fun signUpUser(
        email: String,
        password: String,
    ): Result<Unit> = runCatching {
        userClient.signUp(
            token = pref.fcmToken,
            request = AuthorizeUserRequest(email, password),
        )
    }

    override suspend fun signInUser(
        email: String,
        password: String,
    ): Result<Unit> = runCatching {
        userClient.signIn(
            token = pref.fcmToken,
            request = AuthorizeUserRequest(email, password),
        )
    }.map {
        with(it) {
            if (isSuccess) pref.accessToken = data.accessToken
            else Timber.e(message)
        }
    }

    override suspend fun logoutUser(): Result<Unit> = runCatching {
        userClient.logout().run {
            if (isSuccess) pref.deleteAccessToken()
            else Timber.e(resultMsg)
        }
    }

    override suspend fun withdrawUser(): Result<Unit> = runCatching {
        userClient.withdrawUser(
            accessToken = pref.accessToken,
        ).run {
            if (isSuccess) pref.deleteAccessToken()
            else Timber.e(resultMsg)
        }
    }

    override suspend fun patchPassword(email: String, password: String): Result<Unit> =
        runCatching {
            userClient.patchPassword(
                token = pref.fcmToken,
                request = AuthorizeUserRequest(email, password),
            )
        }

    override suspend fun getCategoryOrders(): List<CategoryOrder> {
        return categoryOrderDao.getCategoryOrders().toDomain()
    }

    override fun getCategoryOrdersAsFlow(): Flow<List<CategoryOrder>> {
        return categoryOrderDao.getCategoryOrdersAsFlow().map { it.toDomain() }
    }

    override suspend fun updateCategoryOrder(order: CategoryOrder) {
        return categoryOrderDao.updateCategoryOrder(order.toEntity())
    }

    override suspend fun updateCategoryOrders(orders: List<CategoryOrder>) {
        return categoryOrderDao.updateCategoryOrders(orders.toEntity())
    }
}