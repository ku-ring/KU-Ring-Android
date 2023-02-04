package com.ku_stacks.ku_ring.ui.notice_storage.ui_model

data class SavedNoticeUiModel(
    val articleId: String,
    val category: String,
    val baseUrl: String,
    val postedDate: String,
    val subject: String,
    val tag: List<String>,
)
