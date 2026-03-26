package com.ku_stacks.ku_ring.firebase.messaging.mapper

import com.ku_stacks.ku_ring.firebase.messaging.model.KuringFcmPayload
import com.ku_stacks.ku_ring.local.entity.NoticeEntity
import com.ku_stacks.ku_ring.local.entity.PushContent
import com.ku_stacks.ku_ring.local.entity.PushEntity

internal fun KuringFcmPayload.Notice.toNoticeEntity() = NoticeEntity(
    id = id,
    articleId = articleId,
    category = category,
    subject = subject,
    postedDate = postedDate,
    url = baseUrl,
    isNew = true,
    isRead = false,
    isSaved = false,
    isImportant = false,
    isReadOnStorage = false
)

internal fun KuringFcmPayload.toPushEntity(
    receivedDate: String,
) = PushEntity(
    isNew = true,
    receivedDate = receivedDate,
    content = when (this) {
        is KuringFcmPayload.Notice -> toContent()
        is KuringFcmPayload.AcademicEvent -> toContent()
        is KuringFcmPayload.Club -> toContent()
        is KuringFcmPayload.Custom -> toContent()
    }
)

private fun KuringFcmPayload.Notice.toContent() = PushContent.Notice(
    id = id,
    articleId = articleId,
    category = category,
    subject = subject,
    fullUrl = baseUrl,
    postedDate = postedDate
)

private fun KuringFcmPayload.AcademicEvent.toContent() = PushContent.Academic(
    title = title,
    body = body,
)

private fun KuringFcmPayload.Club.toContent() = PushContent.Club(
    clubId = clubId,
    title = title,
    body = body,
)

private fun KuringFcmPayload.Custom.toContent() = PushContent.Custom(
    title = title,
    body = body,
)
