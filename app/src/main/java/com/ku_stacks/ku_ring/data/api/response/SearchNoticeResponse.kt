package com.ku_stacks.ku_ring.data.api.response

import com.google.gson.annotations.SerializedName

data class SearchNoticeResponse(
    @SerializedName("articleId") val articleId: String?,
    @SerializedName("postedDate") val postedDate: String?,
    @SerializedName("subject") val subject: String?,
    @SerializedName("baseUrl") val baseUrl: String?,
    @SerializedName("category") val category: String?,
)
