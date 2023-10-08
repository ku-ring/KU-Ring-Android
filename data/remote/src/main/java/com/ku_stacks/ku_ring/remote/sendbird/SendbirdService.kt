package com.ku_stacks.ku_ring.remote.sendbird

import com.ku_stacks.ku_ring.remote.sendbird.response.UserListResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SendbirdService {
    @GET("users")
    fun fetchNicknameList(
        @Query("nickname") nickname: String
    ): Single<UserListResponse>
}