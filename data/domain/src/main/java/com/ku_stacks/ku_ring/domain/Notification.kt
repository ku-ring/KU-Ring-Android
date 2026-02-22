package com.ku_stacks.ku_ring.domain

data class Notification(
    val articleId: String,
    val category: NotificationCategory,
    val postedDate: String,
    val subject: String,
    val fullUrl: String,
    var isNew: Boolean,
    val receivedDate: String,
    val tag: List<String>
)

enum class NotificationCategory(
    val category: String,
) {
    NOTICE("notice"),
    CUSTOM("admin"),
    ACADEMIC_EVENT("academic"),
    UNKNOWN("unknown")
    ;

    companion object {
        fun from(category: String): NotificationCategory {
            return entries.find { it.category == category } ?: UNKNOWN
        }
    }
}
