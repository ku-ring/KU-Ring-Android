package com.ku_stacks.ku_ring.notice.api.response

import com.google.gson.annotations.SerializedName

data class SearchNoticeDataResponse(
    @SerializedName("noticeList") val noticeList: List<SearchNoticeResponse>
)
