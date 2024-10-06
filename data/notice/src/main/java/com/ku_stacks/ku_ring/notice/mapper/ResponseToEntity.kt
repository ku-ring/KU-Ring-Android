package com.ku_stacks.ku_ring.notice.mapper

import com.ku_stacks.ku_ring.local.entity.NoticeEntity
import com.ku_stacks.ku_ring.remote.notice.response.DepartmentNoticeResponse
import com.ku_stacks.ku_ring.remote.notice.response.NoticeResponse

fun List<NoticeResponse>.toEntities(startDate: String) = map { it.toNoticeEntity(startDate) }

fun NoticeResponse.toNoticeEntity(startDate: String) = NoticeEntity(
    articleId = articleId,
    category = category,
    department = "",
    subject = subject,
    postedDate = postedDate,
    url = url,
    isNew = (postedDate >= startDate),
    isRead = false,
    isSaved = false,
    isReadOnStorage = false,
    isImportant = isImportant,
)


fun List<DepartmentNoticeResponse>.toEntityList(shortName: String, startDate: String) =
    map { it.toNoticeEntity(shortName, startDate) }

/**
 * null인 필드가 하나라도 있을 경우 [NullPointerException]을 던진다.
 * TODO: NPE 대신 빈 문자열을 넣도록 수정
 */
fun DepartmentNoticeResponse.toNoticeEntity(shortName: String, startDate: String): NoticeEntity {
    return NoticeEntity(
        articleId = articleId!!,
        category = category!!,
        department = shortName,
        subject = subject!!,
        postedDate = postedDate!!,
        url = url!!,
        isNew = (postedDate!! >= startDate),
        isRead = false,
        isSaved = false,
        isReadOnStorage = false,
        isImportant = important,
    )
}