package com.ku_stacks.ku_ring.notice.mapper

import com.ku_stacks.ku_ring.local.entity.NoticeEntity
import com.ku_stacks.ku_ring.notice.api.response.DepartmentNoticeResponse
import java.text.SimpleDateFormat
import java.util.Locale


fun List<DepartmentNoticeResponse>.toEntityList(shortName: String, startDate: String) =
    map { it.toNoticeEntity(shortName, startDate) }

/**
 * null인 필드가 하나라도 있을 경우 [NullPointerException]을 던진다.
 * TODO: NPE 대신 빈 문자열을 넣도록 수정
 */
fun DepartmentNoticeResponse.toNoticeEntity(shortName: String, startDate: String): NoticeEntity {
    val dashRemovedPostedDate = removeDashFromDate(postedDate!!)
    return NoticeEntity(
        articleId = articleId!!,
        category = category!!,
        department = shortName,
        subject = subject!!,
        postedDate = dashRemovedPostedDate,
        url = url!!,
        isNew = (dashRemovedPostedDate >= startDate),
        isRead = false,
        isSaved = false,
        isReadOnStorage = false,
    )
}

private fun removeDashFromDate(dashSeparatedDate: String): String {
    val inputFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    val date = inputFormatter.parse(dashSeparatedDate)
    return outputFormatter.format(date)
}