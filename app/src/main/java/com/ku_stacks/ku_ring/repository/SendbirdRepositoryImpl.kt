package com.ku_stacks.ku_ring.repository

import com.ku_stacks.ku_ring.data.api.SendbirdClient
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class SendbirdRepositoryImpl @Inject constructor(
    private val sendbirdClient: SendbirdClient
) : SendbirdRepository {

    override fun hasDuplicateNickname(nickname: String, userId: String): Single<Boolean> {
        return sendbirdClient.fetchNicknameList(nickname)
            .subscribeOn(Schedulers.io())
            .map {
                if (it.users.isEmpty()) {
                    return@map false
                } else {
                    for (userResponse in it.users) {
                        if (userResponse.userId == userId) {
                            return@map false
                        }
                    }
                    return@map true
                }
            }
    }
}