package com.ku_stacks.ku_ring.remote.notice.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscribeRequest(
    @SerialName(value = "categories")
    val categories: List<String>
)