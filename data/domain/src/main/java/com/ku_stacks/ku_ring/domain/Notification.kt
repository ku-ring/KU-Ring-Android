package com.ku_stacks.ku_ring.domain

data class Notification(
    val id: Int,
    val category: NotificationCategory,
    val isNew: Boolean,
    val receivedDate: String,
    val content: NotificationContent,
)

sealed interface NotificationContent {
    data class Notice(
        val articleId: String,
        val noticeCategory: String,
        val subject: String,
        val fullUrl: String,
        val postedDate: String,
    ) : NotificationContent

    data class Club(
        val clubId: String,
        val title: String,
        val body: String,
    ) : NotificationContent

    data class Common(
        val title: String,
        val body: String,
    ) : NotificationContent
}

enum class NotificationCategory(
    val category: String,
) {
    NOTICE("notice"),
    CUSTOM("admin"),
    ACADEMIC_EVENT("academic"),
    CLUB("club"),
    UNKNOWN("unknown")
    ;

    companion object {
        fun from(category: String): NotificationCategory {
            return entries.find { it.category == category } ?: UNKNOWN
        }
    }
}
