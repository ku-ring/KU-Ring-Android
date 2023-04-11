package com.ku_stacks.ku_ring.data.api.response

import com.google.gson.annotations.SerializedName

data class DepartmentNoticeDataResponse(
    @SerializedName("noticeList") val noticeList: List<DepartmentNoticeResponse>?
)