package com.ku_stacks.ku_ring.remote.notice.response

data class DepartmentNoticeListResponse(
    val code: Int,
    val message: String,
    val data: List<DepartmentNoticeResponse>,
)