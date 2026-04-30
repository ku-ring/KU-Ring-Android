package com.ku_stacks.ku_ring.remote.notice.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DepartmentNoticeResponse(
    @SerialName("id") val id: Int? = null,
    @SerialName("articleId") val articleId: String?,
    @SerialName("postedDate") val postedDate: String?,
    @SerialName("subject") val subject: String?,
    @SerialName("url") val url: String?,
    @SerialName("category") val category: String?,
    @SerialName("important") val important: Boolean,
    @SerialName("commentCount") val commentCount: Int? = null,
)