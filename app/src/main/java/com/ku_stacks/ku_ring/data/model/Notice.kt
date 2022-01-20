package com.ku_stacks.ku_ring.data.model

data class Notice(
    val postedDate: String,
    val subject: String,
    val category: String,
    val url: String,
    val articleId: String,
    var isNew: Boolean,
    var isRead: Boolean,
    var isSubscribing: Boolean,
    val tag: List<String>
)