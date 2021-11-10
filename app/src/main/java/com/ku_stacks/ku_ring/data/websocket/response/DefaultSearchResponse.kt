package com.ku_stacks.ku_ring.data.websocket.response

import com.google.gson.annotations.SerializedName

data class DefaultSearchResponse(
    @SerializedName(value = "type")
    val type: String
)
