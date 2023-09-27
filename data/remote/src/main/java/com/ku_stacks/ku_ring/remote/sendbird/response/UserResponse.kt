package com.ku_stacks.ku_ring.remote.sendbird.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("user_id")
    val userId: String
) {
    companion object {
        fun mock() = UserResponse(
            nickname = "kuring",
            userId = "kuring"
        )
    }
}