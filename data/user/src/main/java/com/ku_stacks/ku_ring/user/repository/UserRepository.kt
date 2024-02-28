package com.ku_stacks.ku_ring.user.repository

import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface UserRepository {
    fun blockUser(userId: String, nickname: String): Completable
    fun getBlackUserList(): Single<List<String>>
    fun sendFeedback(feedback: String): Single<DefaultResponse>
}