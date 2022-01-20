package com.ku_stacks.ku_ring.data.api.request

import com.google.gson.annotations.SerializedName

data class FeedbackRequest(
    @SerializedName(value = "id")
    val token: String,
    @SerializedName(value = "content")
    val content: String
)
