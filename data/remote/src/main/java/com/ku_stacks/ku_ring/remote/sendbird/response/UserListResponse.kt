package com.ku_stacks.ku_ring.remote.sendbird.response

import com.google.gson.annotations.SerializedName

data class UserListResponse(
    @SerializedName("users")
    val users: List<UserResponse>
) {
    companion object {
        fun mock() = UserListResponse("abcde"
            .map { it.toString() }
            .map { s -> UserResponse(nickname = s, userId = s) }
        )
    }
}