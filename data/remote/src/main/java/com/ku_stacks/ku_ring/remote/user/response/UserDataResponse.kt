package com.ku_stacks.ku_ring.remote.user.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDataResponse(
    @SerialName("code") val code: Int,
    @SerialName("message") val message: String,
    @SerialName("data") val data: UserData,
) {
    @Serializable
    data class UserData(
        @SerialName("email") val email: String,
        @SerialName("nickname") val nickname: String,
    )
}

