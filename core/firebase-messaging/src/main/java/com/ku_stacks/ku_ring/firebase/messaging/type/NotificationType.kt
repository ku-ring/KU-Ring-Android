package com.ku_stacks.ku_ring.firebase.messaging.type

enum class NotificationType(val type: String) {
    NOTICE("notice"),
    CUSTOM("admin"),
    ACADEMIC_EVENT("academic"),
    CLUB("club"),
    ;

    companion object {
        fun from(type: String?): NotificationType {
            return entries.find { it.type == type } ?: CUSTOM
        }
    }
}
