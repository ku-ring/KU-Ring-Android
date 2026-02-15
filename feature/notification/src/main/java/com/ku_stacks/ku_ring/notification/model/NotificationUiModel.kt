package com.ku_stacks.ku_ring.notification.model

import com.ku_stacks.ku_ring.designsystem.R.drawable.ic_calendar_v2
import com.ku_stacks.ku_ring.designsystem.R.drawable.ic_list_v2
import com.ku_stacks.ku_ring.domain.Notification
import com.ku_stacks.ku_ring.domain.NotificationCategory
import com.ku_stacks.ku_ring.domain.NotificationContent
import com.ku_stacks.ku_ring.feature.notification.R.string.notification_category_academic
import com.ku_stacks.ku_ring.feature.notification.R.string.notification_category_custom
import com.ku_stacks.ku_ring.feature.notification.R.string.notification_category_notice

data class NotificationUiModel(
    val notification: Notification,
) {
    val iconRes = when (notification.category) {
        NotificationCategory.NOTICE -> ic_list_v2
        NotificationCategory.ACADEMIC_EVENT -> ic_calendar_v2
        else -> ic_list_v2
    }

    val categoryStringRes = when (notification.category) {
        NotificationCategory.NOTICE -> notification_category_notice
        NotificationCategory.ACADEMIC_EVENT -> notification_category_academic
        else -> notification_category_custom
    }

    val content = when (notification.content) {
        is NotificationContent.Notice -> (notification.content as NotificationContent.Notice).subject
        is NotificationContent.Club -> (notification.content as NotificationContent.Club).title
        is NotificationContent.Common -> (notification.content as NotificationContent.Common).title
    }
}
