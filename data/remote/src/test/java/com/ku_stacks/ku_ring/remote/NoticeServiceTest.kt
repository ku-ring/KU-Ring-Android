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

        val noticeResponse = response.data ?: throw IllegalStateException("Data should not be null")

        // then
        assertEquals(3, noticeResponse.size)

        assertEquals(true, response.isSuccess)
        assertEquals(12345, noticeResponse[0].id)
        assertEquals("5b45b56", noticeResponse[0].articleId)
        assertEquals("bachelor", noticeResponse[0].category)
        assertEquals("2022-01-05", noticeResponse[0].postedDate)
        assertEquals("subject_1", noticeResponse[0].subject)
    }

    @Test
    fun `fetch SubscribeList Test`() = runTest {
        // given
        enqueueResponse("/SubscriptionResponse.json")
        val mockToken = "mockToken"

        // when
        val response = service.fetchSubscribeList(mockToken)
        mockWebServer.takeRequest()

        val categoryList = response.data ?: throw IllegalStateException("Data should not be null")

        // then
        assertEquals(true, response.isSuccess)
        assertEquals(2, categoryList.size)
        assertEquals("student", categoryList[0].name)
        assertEquals("stu", categoryList[0].shortName)
        assertEquals("학생", categoryList[0].koreanName)
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

    @Test
    fun `fetchNotices Test`() = runTest {
        // given
        enqueueResponse("/SearchNoticeResponse.json")

        // when
        val response = service.fetchNotices(content = "수강신청")
        mockWebServer.takeRequest()

        val noticeList =
            response.data?.noticeList ?: throw IllegalStateException("Data should not be null")

        // then
        assertEquals(true, response.isSuccess)
        assertEquals(2, noticeList.size)

        val notice = noticeList[0]
        assertEquals(134016, notice.id)
        assertEquals("1171245", notice.articleId)
        assertEquals("2026-04-01", notice.postedDate)
        assertEquals("department", notice.category)
        assertEquals("https://econ.konkuk.ac.kr/bbs/econ/423/1171245/artclView.do", notice.baseUrl)
        assertEquals(false, notice.isImportant)
        assertEquals(0, notice.commentCount)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetchDepartmentNoticeList Test`() = runTest {
        // given
        enqueueResponse("/DepartmentNoticeResponse.json")

        // when
        val mockResponse = service.fetchDepartmentNoticeList(
            type = "dep",
            shortName = "cse",
            page = 0,
            size = 20,
            important = false,
        )
        mockWebServer.takeRequest()

        val noticeList = mockResponse.data ?: throw IllegalStateException("Data should not be null")

        // then

        assertEquals(200, mockResponse.resultCode)
        assertEquals(20, noticeList.size)

        val notice = noticeList[0]
        assertEquals("182677", notice.articleId)
        assertEquals("2023-05-02", notice.postedDate)
        assertEquals(
            "https://cse.konkuk.ac.kr/bbs/cse/775/182677/artclView.do",
            notice.url
        )
        assertEquals("2023학년도 진로총조사 설문 요청", notice.subject)
        assertEquals("department", notice.category)
        assertEquals(false, notice.important)
    }
}