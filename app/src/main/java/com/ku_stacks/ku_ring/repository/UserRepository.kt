package com.ku_stacks.ku_ring.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface UserRepository {
    fun blockUser(userId: String, nickname: String): Completable
    fun getBlackUserList(): Single<List<String>>
}