package com.ku_stacks.ku_ring.notice.mapper

import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.remote.notice.response.NoticeListResponse
import com.ku_stacks.ku_ring.remote.notice.response.SearchNoticeListResponse

fun NoticeListResponse.toNoticeList(type: String): List<Notice> {
    return if (type == "lib") {
        noticeResponse.map {
            val transformedDate = it.postedDate.let { date ->
                if (date.length == 19) { //도서관의 경우에는 특별하게 millisecond 단위로 나옴
                    return@let date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10)
                } else {
                    return@let date
                }
            }

            Notice(
                postedDate = transformedDate,
                subject = it.subject.trim(),
                category = it.category,
                url = it.url,
                articleId = it.articleId,
                isNew = false,
                isRead = false,
                isSubscribing = false,
                isSaved = false,
                isReadOnStorage = false,
                isImportant = it.isImportant,
                tag = emptyList(),
            )
        }
    } else {
        noticeResponse.map {
            Notice(
                postedDate = it.postedDate,
                subject = it.subject.trim(),
                category = it.category,
                url = it.url,
                articleId = it.articleId,
                isNew = false,
                isRead = false,
                isSubscribing = false,
                isSaved = false,
                isReadOnStorage = false,
                isImportant = it.isImportant,
                tag = emptyList(),
            )
        }
    }
}

fun SearchNoticeListResponse.toNoticeList(): List<Notice> {
    return data?.noticeList?.map {
        return@map Notice(
            postedDate = it.postedDate,
            subject = it.subject,
            category = it.category,
            url = it.baseUrl,
            articleId = it.articleId,
            isNew = false,
            isRead = false,
            isSubscribing = false,
            isSaved = false,
            isReadOnStorage = false,
            isImportant = it.isImportant,
            tag = emptyList()
        )
    } ?: emptyList()
}