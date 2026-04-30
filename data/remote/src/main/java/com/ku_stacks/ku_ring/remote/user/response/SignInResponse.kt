package com.ku_stacks.ku_ring.remote.user.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(
    @SerialName("accessToken") val accessToken: String,
)

