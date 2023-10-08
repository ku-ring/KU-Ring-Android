package com.ku_stacks.ku_ring.remote.sendbird

import com.ku_stacks.ku_ring.remote.sendbird.response.UserListResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SendbirdClient @Inject constructor(
    private val sendbirdService: SendbirdService
) {
    fun fetchNicknameList(nickname: String): Single<UserListResponse> =
        sendbirdService.fetchNicknameList(nickname)
}