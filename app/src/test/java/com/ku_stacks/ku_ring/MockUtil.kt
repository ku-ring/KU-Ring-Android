package com.ku_stacks.ku_ring

import com.ku_stacks.ku_ring.data.api.response.NoticeListResponse
import com.ku_stacks.ku_ring.data.api.response.NoticeResponse
import com.ku_stacks.ku_ring.data.db.NoticeEntity
import com.ku_stacks.ku_ring.data.db.PushEntity

object MockUtil {

    fun mockNoticeList() = NoticeListResponse(
        isSuccess = true,
        resultMsg = "성공",
        resultCode = 200,
        baseUrl = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do",
        noticeResponse = listOf(mockNoticeResponse())
    )

    private fun mockNoticeResponse() = NoticeResponse(
        articleId = "5b4a11b",
        postedDate = "20220203",
        subject = "2022학년도 1학기 재입학 합격자 유의사항 안내",
        category = "bachelor"
    )

    fun mockNoticeEntity() = NoticeEntity(
        articleId = "5b4a11b",
        category = "bachelor",
        isNew = false,
        isRead = false
    )

    fun mockReadNoticeEntity() = NoticeEntity(
        articleId = "5b4a11b",
        category = "bachelor",
        isNew = false,
        isRead = true
    )

    fun mockPushEntity() = PushEntity(
        articleId = "5b4a11b",
        category = "bachelor",
        postedDate = "!",
        subject = "!",
        baseUrl = "!",
        isNew = false,
        receivedDate = "!"
    )
}