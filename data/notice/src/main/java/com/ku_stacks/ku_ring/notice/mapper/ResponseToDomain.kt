package com.ku_stacks.ku_ring.notice.mapper

import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.remote.notice.response.SearchNoticeDataResponse
import com.ku_stacks.ku_ring.remote.util.DefaultResponse

fun DefaultResponse<SearchNoticeDataResponse>.toNoticeList(): List<Notice> {
    return data?.noticeList?.map {
        return@map Notice(
            postedDate = it.postedDate,
            subject = it.subject,
            category = it.category,
            url = it.baseUrl,
            articleId = it.articleId,
            id = it.id,
            isNew = false,
            isRead = false,
            isSubscribing = false,
            isSaved = false,
            isReadOnStorage = false,
            isImportant = it.isImportant,
            tag = emptyList(),
            commentCount = it.commentCount,
        )
    } ?: emptyList()
}