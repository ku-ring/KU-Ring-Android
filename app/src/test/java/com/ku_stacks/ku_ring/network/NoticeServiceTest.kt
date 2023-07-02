package com.ku_stacks.ku_ring.network

import com.ku_stacks.ku_ring.data.api.NoticeService
import com.ku_stacks.ku_ring.data.api.request.SubscribeRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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
        assertEquals(3, response.noticeResponse.size)

        assertEquals(true, response.isSuccess)
        assertEquals("5b45b56", response.noticeResponse[0].articleId)
        assertEquals("student", response.noticeResponse[0].category)
        assertEquals("20220105", response.noticeResponse[0].postedDate)
        assertEquals("subject_1", response.noticeResponse[0].subject)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetchDepartmentNoticeList Test`() = runTest {
        // given
        enqueueResponse("/DepartmentNoticeResponse.json")

        // when
        val mockResponse = service.fetchDepartmentNoticeList(
            type = "dept",
            shortName = "cse",
            page = 0,
            size = 20,
            important = false,
        )
        mockWebServer.takeRequest()

        assertEquals(200, mockResponse.code)
        assertEquals(20, mockResponse.data.size)

        val notice = mockResponse.data[0]
        assertEquals("182677", notice.articleId)
        assertEquals("2023-05-02", notice.postedDate)
        assertEquals(
            "http://cse.konkuk.ac.kr/noticeView.do?siteId=CSE&boardSeq=882&menuSeq=6097&seq=182677",
            notice.url
        )
        assertEquals("2023학년도 진로총조사 설문 요청", notice.subject)
        assertEquals("department", notice.category)
        assertEquals(false, notice.important)
    }
}