package com.ku_stacks.ku_ring.data.mapper

import com.ku_stacks.ku_ring.data.api.response.NoticeListResponse
import com.ku_stacks.ku_ring.data.db.PushEntity
import com.ku_stacks.ku_ring.data.entity.Notice
import com.ku_stacks.ku_ring.data.entity.Push

fun transformNotice(response : NoticeListResponse, type : String): List<Notice> {
    return if(type == "lib") {
        with(response) {
            this.noticeResponse.map {
                val subjectAndTag = splitSubjectAndTag(it.subject.trim())

                val transformedDate = it.postedDate.let { date ->
                    if (date.length == 19) { //도서관의 경우에는 특별하게 millisecond 단위로 나옴
                        return@let date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10)
                    } else {
                        return@let date
                    }
                }

                Notice(
                    postedDate = transformedDate,
                    subject = subjectAndTag.first,
                    category = it.category,
                    url = this.baseUrl + "/" + it.articleId,
                    articleId = it.articleId,
                    isNew = false,
                    isRead = false,
                    isSubscribing = false,
                    tag = subjectAndTag.second
                )
            }
        }
    }
    else with(response) {
        this.noticeResponse.map {
            val subjectAndTag = splitSubjectAndTag(it.subject.trim())

            Notice(
                postedDate = it.postedDate,
                subject = subjectAndTag.first,
                category = it.category,
                url = this.baseUrl + "?id=" + it.articleId,
                articleId = it.articleId,
                isNew = false,
                isRead = false,
                isSubscribing = false,
                tag = subjectAndTag.second
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

        val subjectAndTag = splitSubjectAndTag(it.subject.trim())

        Push(
            articleId = it.articleId,
            category = it.category,
            postedDate = it.postedDate,
            subject = subjectAndTag.first,
            baseUrl = it.baseUrl,
            isNew = it.isNew,
            receivedDate = it.receivedDate,
            isNewDay = isNewDay,
            tag = subjectAndTag.second
        )
    }
}

private fun splitSubjectAndTag(subject: String): Pair<String, List<String>> {
    val tagList = mutableListOf<String>()
    var startIdx = 0

    if (subject.first() == '[') {
        for (currentIdx in 1 until subject.length) {
            if (subject[currentIdx] == ']') {
                tagList.add(subject.substring(startIdx + 1, currentIdx))
                startIdx = currentIdx + 1
                if (currentIdx + 1 == subject.length || subject[currentIdx + 1] != '[') {
                    break
                }
            }
        }
    }
    return if (tagList.size == 0) {
        Pair(subject, emptyList())
    } else {
        Pair(subject.substring(startIdx).trim(), tagList)
    }
}