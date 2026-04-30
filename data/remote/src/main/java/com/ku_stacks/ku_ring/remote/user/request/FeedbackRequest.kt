package com.ku_stacks.ku_ring.remote.user.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedbackRequest(
    @SerialName(value = "content")
    val content: String
)
