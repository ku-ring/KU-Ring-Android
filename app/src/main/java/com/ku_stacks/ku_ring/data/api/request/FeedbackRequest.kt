package com.ku_stacks.ku_ring.data.api.request

import com.google.gson.annotations.SerializedName

data class FeedbackRequest(
    @SerializedName(value = "content")
    val content: String
)
