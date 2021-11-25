package com.ku_stacks.ku_ring.data.entity

import com.google.gson.annotations.SerializedName

data class Push(
    @SerializedName(value = "articleId")
    val articleId: String,
    @SerializedName(value = "category")
    val category: String,
    @SerializedName(value = "postedDate")
    val postedDate: String,
    @SerializedName(value = "subject")
    val subject: String,
    @SerializedName(value = "baseUrl")
    val baseUrl: String,
    @SerializedName(value = "isNew")
    var isNew: Boolean,
    @SerializedName(value = "receivedDate")
    val receivedDate: String,
    @SerializedName(value = "isNewDay")
    val isNewDay: Boolean,
    @SerializedName(value = "tag")
    val tag: List<String>
)