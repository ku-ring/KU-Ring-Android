package com.ku_stacks.ku_ring.data.api.response

import com.google.gson.annotations.SerializedName

data class SearchNoticeListResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<SearchNoticeResponse>?,
) {
    val isSuccess: Boolean
        get() = (code == 200)
}