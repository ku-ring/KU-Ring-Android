package com.ku_stacks.ku_ring.data.api.response

import com.google.gson.annotations.SerializedName

data class SubscribeListResponse(
    @SerializedName(value = "isSuccess")
    val isSuccess: Boolean,
    @SerializedName(value = "resultMsg")
    val resultMsg: String,
    @SerializedName(value = "resultCode")
    val resultCode: Int,
    @SerializedName(value = "categories")
    val categoryList: List<String>
)
