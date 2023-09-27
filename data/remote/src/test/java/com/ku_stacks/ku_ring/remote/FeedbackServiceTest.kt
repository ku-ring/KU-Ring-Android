package com.ku_stacks.ku_ring.remote

import com.ku_stacks.ku_ring.remote.user.FeedbackService
import com.ku_stacks.ku_ring.remote.user.request.FeedbackRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FeedbackServiceTest : ApiAbstract<FeedbackService>() {

    private lateinit var service: FeedbackService

    @Before
    fun initService() {
        super.createMockServer()
        service = createService(FeedbackService::class.java)
    }

    @After
    fun tearDown() {
        super.stopServer()
    }

    @Test
    fun `send Feedback Test`() {
        // given
        enqueueResponse("/DefaultResponse.json")

        // when
        val token = "mockToken"
        val mockRequest = FeedbackRequest(
            content = "쿠링은 좋은 어플리케이션입니다."
        )
        val response = service.sendFeedback(token, mockRequest).blockingGet()
        mockWebServer.takeRequest()

        // then
        assertEquals(true, response.isSuccess)
        assertEquals("성공", response.resultMsg)
        assertEquals(null, response.data)
    }
}