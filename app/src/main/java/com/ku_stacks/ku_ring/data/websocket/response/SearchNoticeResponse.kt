package com.ku_stacks.ku_ring.data.websocket.response

import com.google.gson.annotations.SerializedName

data class SearchNoticeResponse(
    @SerializedName(value = "articleId")
    val articleId: String,
    @SerializedName(value = "postedDate")
    val postedDate: String,
    @SerializedName(value = "subject")
    val subject: String,
    @SerializedName(value = "category")
    val category: String,
    @SerializedName(value = "baseUrl")
    val baseUrl: String
)