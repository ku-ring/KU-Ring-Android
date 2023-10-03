package com.ku_stacks.ku_ring.remote.notice.response

import com.google.gson.annotations.SerializedName

data class SubscribeListResponse(
    @SerializedName(value = "message")
    val resultMsg: String,
    @SerializedName(value = "code")
    val resultCode: Int,
    @SerializedName(value = "data")
    val categoryList: List<CategoryResponse>
) {
    val isSuccess: Boolean
        get() = (resultCode == 200)
}