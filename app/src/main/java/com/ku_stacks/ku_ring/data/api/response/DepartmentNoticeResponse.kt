package com.ku_stacks.ku_ring.data.api.response

data class DepartmentNoticeResponse(
    val articleId: String,
    val postedDate: String,
    val subject: String,
    val url: String,
    val category: String,
    val important: Boolean,
)