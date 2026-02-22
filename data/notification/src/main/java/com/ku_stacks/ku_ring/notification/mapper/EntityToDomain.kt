package com.ku_stacks.ku_ring.notification.mapper

import com.ku_stacks.ku_ring.domain.Notification
import com.ku_stacks.ku_ring.domain.NotificationCategory
import com.ku_stacks.ku_ring.local.entity.PushEntity

internal fun PushEntity.toDomain() = Notification(
    articleId = articleId,
    category = NotificationCategory.from(category),
    postedDate = postedDate,
    subject = subject,
    fullUrl = fullUrl,
    isNew = isNew,
    receivedDate = receivedDate,
    tag = listOf() // TODO: tag에 매핑할 요소 식별
)
