package com.ku_stacks.ku_ring.remote.notice.response

data class DepartmentNoticeResponse(
    val id: Int?,
    val articleId: String?,
    val postedDate: String?,
    val subject: String?,
    val url: String?,
    val category: String?,
    val important: Boolean,
    val commentCount: Int?,
)