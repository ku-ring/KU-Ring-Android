package com.ku_stacks.ku_ring.repository

import io.reactivex.rxjava3.core.Single

interface SendbirdRepository {
    fun hasDuplicateNickname(nickname: String): Single<Boolean>
}