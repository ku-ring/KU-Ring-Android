package com.ku_stacks.ku_ring.notice.api.response

import com.google.gson.annotations.SerializedName

data class SearchNoticeListResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: SearchNoticeDataResponse?,
) {
    val isSuccess: Boolean
        get() = (code == 200)
}