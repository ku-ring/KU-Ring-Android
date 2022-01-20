package com.ku_stacks.ku_ring.data.api.response

import com.google.gson.annotations.SerializedName

data class SubscribeRequest(
    @SerializedName(value = "id")
    val token: String,
    @SerializedName(value = "categories")
    val categories: List<String>
)