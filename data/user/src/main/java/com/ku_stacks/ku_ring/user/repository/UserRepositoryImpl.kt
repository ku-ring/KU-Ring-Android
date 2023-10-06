package com.ku_stacks.ku_ring.user.repository

import com.ku_stacks.ku_ring.local.entity.BlackUserEntity
import com.ku_stacks.ku_ring.local.room.BlackUserDao
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.remote.user.FeedbackClient
import com.ku_stacks.ku_ring.remote.user.request.FeedbackRequest
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dao: BlackUserDao,
    private val feedbackClient: FeedbackClient,
    private val pref: PreferenceUtil,
) : UserRepository {
    override fun blockUser(userId: String, nickname: String): Completable {
        return dao.blockUser(
            BlackUserEntity(
                userId = userId,
                nickname = nickname,
                blockedAt = System.currentTimeMillis()
            )
        )
    }

    override fun getBlackUserList(): Single<List<String>> {
        return dao.getBlackList()
            .map { blackList ->
                blackList.map { user -> user.userId }
            }
    }

    override fun sendFeedback(feedback: String): Single<DefaultResponse> {
        return feedbackClient.sendFeedback(
            token = pref.fcmToken ?: "",
            feedbackRequest = FeedbackRequest(feedback)
        )
    }
}