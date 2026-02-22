package com.ku_stacks.ku_ring.notification.mapper

import com.ku_stacks.ku_ring.domain.Notification
import com.ku_stacks.ku_ring.domain.NotificationCategory
import com.ku_stacks.ku_ring.domain.NotificationContent
import com.ku_stacks.ku_ring.local.entity.PushContent
import com.ku_stacks.ku_ring.local.entity.PushEntity

internal fun PushEntity.toDomain() = Notification(
    id = id,
    category = content.toCategory(),
    isNew = isNew,
    receivedDate = receivedDate,
    content = content.toDomain(),
)

private fun PushContent.toCategory(): NotificationCategory = when (this) {
    is PushContent.Notice -> NotificationCategory.NOTICE
    is PushContent.Club -> NotificationCategory.CLUB
    is PushContent.Common -> NotificationCategory.from(type)
}

private fun PushContent.toDomain(): NotificationContent = when (this) {
    is PushContent.Notice -> NotificationContent.Notice(
        articleId = articleId,
        noticeCategory = category,
        subject = subject,
        fullUrl = fullUrl,
        postedDate = postedDate,
    )
    is PushContent.Club -> NotificationContent.Club(
        clubId = clubId,
        title = title,
        body = body,
    )
    is PushContent.Common -> NotificationContent.Common(
        title = title,
        body = body,
    )
}
