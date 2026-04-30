package com.ku_stacks.ku_ring.remote.user.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserRequest(val token: String)
