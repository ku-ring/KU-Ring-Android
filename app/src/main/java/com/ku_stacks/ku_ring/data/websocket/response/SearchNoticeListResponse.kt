package com.ku_stacks.ku_ring.data.websocket.response

import com.google.gson.annotations.SerializedName

data class SearchNoticeListResponse(
    @SerializedName(value = "isSuccess")
    val isSuccess: Boolean,
    @SerializedName(value = "resultMsg")
    val resultMsg: String,
    @SerializedName(value = "resultCode")
    val resultCode: Int,
    @SerializedName(value = "noticeList")
    val noticeList: List<SearchNoticeResponse>
)
