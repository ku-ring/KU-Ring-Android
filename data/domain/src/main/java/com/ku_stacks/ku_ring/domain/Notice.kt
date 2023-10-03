package com.ku_stacks.ku_ring.domain

data class Notice(
    val postedDate: String,
    val subject: String,
    val category: String,
    val department: String = "",
    val url: String,
    val articleId: String,
    var isNew: Boolean,
    var isRead: Boolean,
    var isSubscribing: Boolean,
    val isSaved: Boolean,
    val isReadOnStorage: Boolean,
    val tag: List<String>
)