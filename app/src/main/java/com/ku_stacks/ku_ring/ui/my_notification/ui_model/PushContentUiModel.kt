package com.ku_stacks.ku_ring.ui.my_notification.ui_model

class PushContentUiModel (
    val articleId: String,
    val category: String,
    val postedDate: String,
    val subject: String,
    val baseUrl: String,
    val isNew: Boolean,
    val receivedDate: String,
    val tag: List<String>
) : NotificationUiModel()