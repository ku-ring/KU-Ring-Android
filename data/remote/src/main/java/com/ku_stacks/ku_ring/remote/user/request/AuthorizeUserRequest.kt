package com.ku_stacks.ku_ring.remote.user.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorizeUserRequest(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
)