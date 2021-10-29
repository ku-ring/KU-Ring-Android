package com.ku_stacks.ku_ring.data.mapper

import com.ku_stacks.ku_ring.data.api.response.NoticeListResponse
import com.ku_stacks.ku_ring.data.db.PushEntity
import com.ku_stacks.ku_ring.data.entity.Notice
import com.ku_stacks.ku_ring.data.entity.Push

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

//이전 Item과 날짜를 비교해서 날짜 표시 여부 계산
fun transformPush(pushEntityList: List<PushEntity>): List<Push> {
    return pushEntityList.mapIndexed { idx, it ->
        val isNewDay = if (idx == 0) {
            true
        } else {
            val prevItem = pushEntityList[idx - 1]
            prevItem.postedDate != it.postedDate
        }

        Push(
            articleId = it.articleId,
            category = it.category,
            postedDate = it.postedDate,
            subject = it.subject,
            baseUrl = it.baseUrl,
            isNew = it.isNew,
            receivedDate = it.receivedDate,
            isNewDay = isNewDay
        )
    }
}