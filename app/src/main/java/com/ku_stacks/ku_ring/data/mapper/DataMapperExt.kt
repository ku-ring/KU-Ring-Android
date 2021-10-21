package com.ku_stacks.ku_ring.data.mapper

import com.ku_stacks.ku_ring.data.api.response.NoticeListResponse
import com.ku_stacks.ku_ring.data.entity.Notice

fun transformNotice(response : NoticeListResponse): List<Notice> {
    return with(response) {
        this.noticeResponse.map {
            Notice(
                postedDate = it.postedDate,
                subject = it.subject,
                category = it.category,
                url = this.baseUrl + "?id=" + it.articleId,
                articleId = it.articleId,
                isRead = false,
                isNew = false
            )
        }
    }
}