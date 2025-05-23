package com.ku_stacks.ku_ring.remote.notice.response

import com.google.gson.annotations.SerializedName

data class SearchNoticeResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("articleId") val articleId: String,
    @SerializedName("postedDate") val postedDate: String,
    @SerializedName("subject") val subject: String,
    @SerializedName("baseUrl") val baseUrl: String,
    @SerializedName("category") val category: String,
    @SerializedName("important") val isImportant: Boolean,
    @SerializedName("commentCount") val commentCount: Int,
)
