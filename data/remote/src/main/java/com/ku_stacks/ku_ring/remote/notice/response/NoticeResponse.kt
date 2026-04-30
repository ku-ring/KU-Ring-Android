package com.ku_stacks.ku_ring.remote.notice.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoticeResponse(
    @SerialName(value = "id")
    val id: Int,
    @SerialName(value = "articleId")
    val articleId: String,
    @SerialName(value = "postedDate")
    val postedDate: String,
    @SerialName(value = "subject")
    val subject: String,
    @SerialName(value = "url")
    val url: String,
    @SerialName(value = "category")
    val category: String,
    @SerialName("important")
    val isImportant: Boolean,
    @SerialName("commentCount")
    val commentCount: Int = 0,
)