package com.ku_stacks.ku_ring.domain

data class Push(
    val articleId: String,
    val category: String,
    val postedDate: String,
    val subject: String,
    val fullUrl: String,
    var isNew: Boolean,
    val receivedDate: String,
    val tag: List<String>
)