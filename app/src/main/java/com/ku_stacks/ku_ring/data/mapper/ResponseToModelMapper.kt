package com.ku_stacks.ku_ring.data.mapper

import com.ku_stacks.ku_ring.data.api.response.NoticeListResponse
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.data.model.Staff
import com.ku_stacks.ku_ring.data.websocket.response.SearchNoticeListResponse
import com.ku_stacks.ku_ring.data.websocket.response.SearchStaffListResponse

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
                isSaved = false,
                isReadOnStorage = false,
                tag = subjectAndTag.second
            )
        }
    } else {
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
                isSaved = false,
                isReadOnStorage = false,
                tag = subjectAndTag.second
            )
        }
    }
}

fun SearchStaffListResponse.toStaffList(): List<Staff> {
    return staffList.map {
        return@map Staff(
            name = it.name,
            major = it.major,
            lab = it.lab,
            phone = it.phone,
            email = it.email,
            department = it.department,
            college = it.college
        )
    }
}

fun SearchNoticeListResponse.toNoticeList(): List<Notice> {
    return noticeList.map {
        val url = if (it.category == "library") {
            "${it.baseUrl}/${it.articleId}"
        } else {
            "${it.baseUrl}?id=${it.articleId}"
        }

        return@map Notice(
            postedDate = it.postedDate,
            subject = it.subject,
            category = it.category,
            url = url,
            articleId = it.articleId,
            isNew = false,
            isRead = false,
            isSubscribing = false,
            isSaved = false,
            isReadOnStorage = false,
            tag = emptyList()
        )
    }
}

fun com.ku_stacks.ku_ring.data.api.response.SearchNoticeListResponse.toNoticeList(): List<Notice> {
    return data.noticeList.map {
        val url = if (it.category == "library") {
            "${it.baseUrl}/${it.articleId}"
        } else {
            "${it.baseUrl}?id=${it.articleId}"
        }

        return@map Notice(
            postedDate = it.postedDate,
            subject = it.subject,
            category = it.category,
            url = url,
            articleId = it.articleId,
            isNew = false,
            isRead = false,
            isSubscribing = false,
            isSaved = false,
            isReadOnStorage = false,
            tag = emptyList()
        )
    }
}

fun com.ku_stacks.ku_ring.data.api.response.SearchStaffListResponse.toStaffList(): List<Staff> {
    return data.staffList.map {
        Staff(
            name = it.name,
            major = it.major,
            lab = it.lab,
            phone = it.phone,
            email = it.email,
            department = it.deptName,
            college = it.collegeName,
        )
    }
}