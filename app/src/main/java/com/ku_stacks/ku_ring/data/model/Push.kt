package com.ku_stacks.ku_ring.data.model

data class Push(
    val articleId: String,
    val category: String,
    val postedDate: String,
    val subject: String,
    val baseUrl: String,
    var isNew: Boolean,
    val receivedDate: String,
    val isNewDay: Boolean,
    val tag: List<String>
)