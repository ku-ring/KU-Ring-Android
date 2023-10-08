package com.ku_stacks.ku_ring.remote.sendbird.response

import com.google.gson.annotations.SerializedName

data class UserListResponse(
    @SerializedName("users")
    val users: List<UserResponse>
)