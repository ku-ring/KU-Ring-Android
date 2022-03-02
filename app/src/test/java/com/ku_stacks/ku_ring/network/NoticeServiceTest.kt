package com.ku_stacks.ku_ring.network

import com.ku_stacks.ku_ring.data.api.NoticeService
import com.ku_stacks.ku_ring.data.api.request.SubscribeRequest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NoticeServiceTest : ApiAbstract<NoticeService>() {

    private lateinit var service: NoticeService

    @Before
    fun initService() {
        service = createService(NoticeService::class.java)
    }

    @Test
    fun `fetch Bachelor Notice From Network Test`() {
        // given
        enqueueResponse("/NoticeResponse.json")

        // when
        val response = service.fetchNoticeList("bch", 0, 20)
            .blockingGet()
        mockWebServer.takeRequest()

        // then
        assertEquals(11, response.noticeResponse.size)

        assertEquals(true, response.isSuccess)
        assertEquals("https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do", response.baseUrl)
        assertEquals("5b49d36", response.noticeResponse[0].articleId)
        assertEquals("bachelor", response.noticeResponse[0].category)
        assertEquals("20220105", response.noticeResponse[0].postedDate)
        assertEquals(" 2022학년도 1학기 캠퍼스간 다전공 신청 안내(서울→ GLOCAL)", response.noticeResponse[0].subject)
    }

    @Test
    fun `fetch SubscribeList Test`() {
        // given
        enqueueResponse("/SubscriptionResponse.json")
        val mockToken = "mockToken"

        // when
        val response = service.fetchSubscribeList(mockToken)
            .blockingGet()
        mockWebServer.takeRequest()

        // then
        assertEquals(true, response.isSuccess)
        assertEquals(1, response.categoryList.size)
        assertEquals("bachelor", response.categoryList[0])
    }

    @Test
    fun `save SubscribeList Test`() {
        // given
        enqueueResponse("/DefaultResponse.json")

        // when
        val mockRequest = SubscribeRequest(
            token = "mockToken",
            categories = listOf("bachelor")
        )
        val response = service.saveSubscribeList(mockRequest).blockingGet()
        mockWebServer.takeRequest()

        // then
        assertEquals(true, response.isSuccess)
        assertEquals("성공", response.resultMsg)
        assertEquals(201, response.resultCode)
    }
}