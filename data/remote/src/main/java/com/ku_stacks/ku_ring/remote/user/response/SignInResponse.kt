package com.ku_stacks.ku_ring.remote.user.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(
    @SerialName("code") val code: Int,
    @SerialName("message") val message: String,
    @SerialName("data") val data: SignInToken,
) {
    @Serializable
    data class SignInToken(
        @SerialName("accessToken") val accessToken: String,
    )

    val isSuccess: Boolean get() = (code == 200)
}

