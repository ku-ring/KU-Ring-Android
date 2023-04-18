package com.ku_stacks.ku_ring.data.mapper

import com.ku_stacks.ku_ring.data.api.response.DepartmentNoticeResponse
import com.ku_stacks.ku_ring.data.db.NoticeEntity


fun List<DepartmentNoticeResponse>.toEntityList(shortName: String) =
    map { it.toNoticeEntity(shortName) }

/**
 * null인 필드가 하나라도 있을 경우 [NullPointerException]을 던진다.
 */
fun DepartmentNoticeResponse.toNoticeEntity(shortName: String) = NoticeEntity(
    articleId = articleId!!,
    category = category!!,
    department = shortName,
    subject = subject!!,
    postedDate = postedDate!!,
    url = url!!,
    isNew = true,
    isRead = false,
    isSaved = false,
    isReadOnStorage = false,
)