package com.ku_stacks.ku_ring.data.mapper

import com.ku_stacks.ku_ring.data.api.response.NoticeListResponse
import com.ku_stacks.ku_ring.data.entity.Notice

fun transformNotice(response : NoticeListResponse, type : String): List<Notice> {
    return if(type == "lib") {
        with(response) {
            this.noticeResponse.map {
                Notice(
                    postedDate = it.postedDate,
                    subject = it.subject,
                    category = it.category,
                    url = this.baseUrl + "/" + it.articleId,
                    articleId = it.articleId,
                    isNew = false,
                    isRead = false
                )
            }
        }
    }
    else with(response) {
        this.noticeResponse.map {
            Notice(
                postedDate = it.postedDate,
                subject = it.subject,
                category = it.category,
                url = this.baseUrl + "?id=" + it.articleId,
                articleId = it.articleId,
                isNew = false,
                isRead = false
            )
        }
    }
}