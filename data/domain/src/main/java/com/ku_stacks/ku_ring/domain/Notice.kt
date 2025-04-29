package com.ku_stacks.ku_ring.domain

// TODO: isRead와 isReadOnStorage 통합하기 (실질적으로 사용처가 같음)
data class Notice(
    val postedDate: String,
    val subject: String,
    val category: String,
    val department: String = "",
    val url: String,
    val articleId: String,
    val id: Int,
    var isNew: Boolean,
    var isRead: Boolean,
    var isSubscribing: Boolean,
    val isSaved: Boolean,
    val isReadOnStorage: Boolean,
    val isImportant: Boolean,
    val tag: List<String>
)