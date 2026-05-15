package com.ku_stacks.ku_ring.remote.notice.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchNoticeResponse(
    @SerialName("id") val id: Int,
    @SerialName("articleId") val articleId: String,
    @SerialName("postedDate") val postedDate: String,
    @SerialName("subject") val subject: String,
    @SerialName("baseUrl") val baseUrl: String,
    @SerialName("category") val category: String,
    @SerialName("important") val isImportant: Boolean = false,
    @SerialName("commentCount") val commentCount: Int,
)
