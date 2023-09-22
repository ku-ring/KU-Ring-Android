package com.ku_stacks.ku_ring.notice.api.response

data class DepartmentNoticeListResponse(
    val code: Int,
    val message: String,
    val data: List<DepartmentNoticeResponse>,
)