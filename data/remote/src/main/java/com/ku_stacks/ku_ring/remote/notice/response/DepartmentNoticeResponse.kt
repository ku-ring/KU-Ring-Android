package com.ku_stacks.ku_ring.remote.notice.response

import kotlinx.serialization.Serializable

@Serializable
data class DepartmentNoticeResponse(
    val id: Int? = null,
    val articleId: String?,
    val postedDate: String?,
    val subject: String?,
    val url: String?,
    val category: String?,
    val important: Boolean,
    val commentCount: Int? = null,
)