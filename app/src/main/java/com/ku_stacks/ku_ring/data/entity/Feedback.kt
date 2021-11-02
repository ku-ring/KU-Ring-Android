package com.ku_stacks.ku_ring.data.entity

import com.google.gson.annotations.SerializedName

data class Feedback(
    @SerializedName(value = "id")
    val token: String,
    @SerializedName(value = "content")
    val content: String
)
