package com.ku_stacks.ku_ring.firebase.messaging.mapper

import com.ku_stacks.ku_ring.firebase.messaging.type.NotificationType
import com.ku_stacks.ku_ring.local.entity.PushContent
import com.ku_stacks.ku_ring.local.entity.PushEntity

/**
 * FCM을 통해 전달받은 데이터를 PushEntity로 변환하는 함수입니다.
 *
 * @param data FCM을 통해 전달받은 데이터
 * @return 변환된 PushEntity 객체
 * @throws IllegalArgumentException 유효하지 않은 메시지 타입일 경우 발생
 */
internal fun getPushEntity(
    data: Map<String, String?>,
    receivedDate: String,
): PushEntity {
    val type = NotificationType.from(data["type"])

    return when (type) {
        NotificationType.NOTICE -> getNoticePushEntity(data, receivedDate)
        NotificationType.ACADEMIC_EVENT -> getAcademicEventPushEntity(data, receivedDate)
        NotificationType.CLUB -> getClubPushEntity(data, receivedDate)
        NotificationType.CUSTOM -> getCustomPushEntity(data, receivedDate)
    }
}

private fun getNoticePushEntity(
    data: Map<String, String?>,
    receivedDate: String,
): PushEntity {
    val id = data["id"]?.toInt() ?: 0
    val articleId = data["articleId"]!!
    val category = data["category"]!!
    val postedDate = data["postedDate"]!!
    val subject = data["subject"]!!
    val fullUrl = data["baseUrl"]!!

    return PushEntity(
        isNew = true,
        receivedDate = receivedDate,
        content = PushContent.Notice(
            id = id,
            articleId = articleId,
            category = category,
            subject = subject,
            fullUrl = fullUrl,
            postedDate = postedDate
        )
    )
}

private fun getClubPushEntity(
    data: Map<String, String?>,
    receivedDate: String,
): PushEntity {
    val clubId = data["clubId"]!!
    val title = data["title"]!!
    val body = data["body"]!!

    return PushEntity(
        isNew = true,
        receivedDate = receivedDate,
        content = PushContent.Club(
            clubId = clubId,
            title = title,
            body = body,
        )
    )
}

private fun getAcademicEventPushEntity(
    data: Map<String, String?>,
    receivedDate: String,
): PushEntity {
    val type = data["type"]!!
    val title = data["title"]!!
    val body = data["body"]!!

    return PushEntity(
        isNew = true,
        receivedDate = receivedDate,
        content = PushContent.Common(
            type = type,
            title = title,
            body = body,
        )
    )
}

private fun getCustomPushEntity(
    data: Map<String, String?>,
    receivedDate: String,
): PushEntity {
    val type = data["type"]!!
    val title = data["title"]!!
    val body = data["body"]!!

    return PushEntity(
        isNew = true,
        receivedDate = receivedDate,
        content = PushContent.Common(
            type = type,
            title = title,
            body = body,
        )
    )
}
