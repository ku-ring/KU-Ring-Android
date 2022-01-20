package com.ku_stacks.ku_ring.network

import com.ku_stacks.ku_ring.data.api.NoticeService
import com.ku_stacks.ku_ring.data.api.response.NoticeResponse
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NoticeServiceTest : ApiAbstract<NoticeService>() {

    private lateinit var service: NoticeService

    @Before
    fun initService() {
        service = createNoticeService(NoticeService::class.java)
    }

    @Test
    fun `fetch Bachelor Notice From Network Test`() {
        enqueueResponse("/NoticeResponse.json")
        val response = service.fetchNoticeList("bch", 0, 20)
            .blockingGet()
        mockWebServer.takeRequest()

        assertEquals(11, response.noticeResponse.size)

        assertEquals(true, response.isSuccess)
        assertEquals("https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do", response.baseUrl)
        assertEquals("5b49d36", response.noticeResponse[0].articleId)
        assertEquals("bachelor", response.noticeResponse[0].category)
        assertEquals("20220105", response.noticeResponse[0].postedDate)
        assertEquals(" 2022학년도 1학기 캠퍼스간 다전공 신청 안내(서울→ GLOCAL)", response.noticeResponse[0].subject)
    }
}