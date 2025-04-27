package com.ku_stacks.ku_ring.remote

import com.ku_stacks.ku_ring.remote.notice.NoticeService
import com.ku_stacks.ku_ring.remote.notice.request.SubscribeRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NoticeServiceTest : ApiAbstract<NoticeService>() {

    private lateinit var service: NoticeService

    @Before
    fun initService() {
        super.createMockServer()
        service = createService(NoticeService::class.java)
    }

    @After
    fun tearDown() {
        super.stopServer()
    }

    @Test
    fun `fetch Bachelor Notice From Network Test`() = runTest {
        // given
        enqueueResponse("/NoticeResponse.json")

        // when
        val response = service.fetchNoticeList("bch", 0, 20)
        mockWebServer.takeRequest()

        // then
        assertEquals(3, response.noticeResponse.size)

        assertEquals(true, response.isSuccess)
        assertEquals(12345, response.noticeResponse[0].id)
        assertEquals("5b45b56", response.noticeResponse[0].articleId)
        assertEquals("student", response.noticeResponse[0].category)
        assertEquals("20220105", response.noticeResponse[0].postedDate)
        assertEquals("subject_1", response.noticeResponse[0].subject)
    }

    @Test
    fun `fetch SubscribeList Test`() = runTest {
        // given
        enqueueResponse("/SubscriptionResponse.json")
        val mockToken = "mockToken"

        // when
        val response = service.fetchSubscribeList(mockToken)
        mockWebServer.takeRequest()

        // then
        assertEquals(true, response.isSuccess)
        assertEquals(2, response.categoryList.size)
        assertEquals("student", response.categoryList[0].name)
        assertEquals("stu", response.categoryList[0].shortName)
        assertEquals("학생", response.categoryList[0].koreanName)
    }

    @Test
    fun `save SubscribeList Test`() = runTest {
        // given
        enqueueResponse("/DefaultResponse.json")

        // when
        val mockRequest = SubscribeRequest(
            categories = listOf("bachelor")
        )
        val response = service.saveSubscribeList(token = "mockToken", mockRequest)
        mockWebServer.takeRequest()

        // then
        assertEquals(true, response.isSuccess)
        assertEquals("성공", response.resultMsg)
        assertEquals(200, response.resultCode)
        assertEquals(null, response.data)
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