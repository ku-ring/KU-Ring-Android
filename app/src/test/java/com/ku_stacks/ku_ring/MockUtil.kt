package com.ku_stacks.ku_ring

import com.ku_stacks.ku_ring.data.api.response.NoticeListResponse
import com.ku_stacks.ku_ring.data.api.response.NoticeResponse

object MockUtil {

    fun mockNoticeList() = NoticeListResponse(
        isSuccess = true,
        resultMsg = "성공",
        resultCode = 200,
        baseUrl = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do",
        noticeResponse = listOf(mockNoticeResponse())
    )

    fun mockNoticeResponse() = NoticeResponse(
        articleId = "5b4a11b",
        postedDate = "20220203",
        subject = "2022학년도 1학기 재입학 합격자 유의사항 안내",
        category = "bachelor"
    )
}