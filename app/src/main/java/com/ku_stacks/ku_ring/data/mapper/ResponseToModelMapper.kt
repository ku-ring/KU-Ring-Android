package com.ku_stacks.ku_ring.data.mapper

import com.ku_stacks.ku_ring.data.api.response.NoticeListResponse
import com.ku_stacks.ku_ring.data.entity.Notice
import com.ku_stacks.ku_ring.data.entity.Staff
import com.ku_stacks.ku_ring.data.websocket.response.SearchStaffResponse

fun NoticeListResponse.toNoticeList(type: String): List<Notice> {
    return if (type == "lib") {
        noticeResponse.map {
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
    else {
        noticeResponse.map {
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

fun SearchStaffResponse.toStaff(): Staff {
    return Staff(
        name = name,
        major = major,
        lab = lab,
        phone = phone,
        email = email,
        department = department,
        college = college
    )
}