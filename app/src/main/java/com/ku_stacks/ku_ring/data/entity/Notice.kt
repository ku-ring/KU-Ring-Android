package com.ku_stacks.ku_ring.data.entity

import com.google.gson.annotations.SerializedName

data class Notice(
    @SerializedName(value = "postedDate")
    val postedDate: String,
    @SerializedName(value = "subject")
    val subject: String,
    @SerializedName(value = "category")
    val category: String,
    @SerializedName(value = "url")
    val url: String,
    @SerializedName(value = "articleId")
    val articleId: String,
    @SerializedName(value = "isNew")
    var isNew: Boolean,
    @SerializedName(value = "isRead")
    var isRead: Boolean,
    @SerializedName(value = "isSubscribing")
    var isSubscribing: Boolean
)