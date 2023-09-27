package com.ku_stacks.ku_ring.remote.user.request

import com.google.gson.annotations.SerializedName

data class FeedbackRequest(
    @SerializedName(value = "content")
    val content: String
)
