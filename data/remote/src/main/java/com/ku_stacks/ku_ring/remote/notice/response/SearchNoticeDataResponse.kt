package com.ku_stacks.ku_ring.remote.notice.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchNoticeDataResponse(
    @SerialName("noticeList") val noticeList: List<SearchNoticeResponse>
)
