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
) = when (this) {
    is KuringFcmPayload.Notice -> toNoticePushEntity(receivedDate)
    is KuringFcmPayload.AcademicEvent -> toAcademicEventPushEntity(receivedDate)
    is KuringFcmPayload.Club -> toClubPushEntity(receivedDate)
    is KuringFcmPayload.Custom -> toCustomPushEntity(receivedDate)
}

private fun KuringFcmPayload.Notice.toNoticePushEntity(
    receivedDate: String,
) = PushEntity(
    isNew = true,
    receivedDate = receivedDate,
    content = PushContent.Notice(
        id = id,
        articleId = articleId,
        category = category,
        subject = subject,
        fullUrl = baseUrl,
        postedDate = postedDate
    )
)

private fun KuringFcmPayload.AcademicEvent.toAcademicEventPushEntity(
    receivedDate: String,
) = PushEntity(
    isNew = true,
    receivedDate = receivedDate,
    content = PushContent.Academic(
        title = title,
        body = body,
    )
)

private fun KuringFcmPayload.Club.toClubPushEntity(
    receivedDate: String,
) = PushEntity(
    isNew = true,
    receivedDate = receivedDate,
    content = PushContent.Club(
        clubId = clubId,
        title = title,
        body = body,
    )
)

private fun KuringFcmPayload.Custom.toCustomPushEntity(
    receivedDate: String,
) = PushEntity(
    isNew = true,
    receivedDate = receivedDate,
    content = PushContent.Custom(
        title = title,
        body = body,
    )
)
