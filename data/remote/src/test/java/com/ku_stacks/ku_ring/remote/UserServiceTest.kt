package com.ku_stacks.ku_ring.remote

import com.ku_stacks.ku_ring.remote.user.UserService
import com.ku_stacks.ku_ring.remote.user.request.FeedbackRequest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserServiceTest : ApiAbstract<UserService>() {

    private lateinit var service: UserService

    @Before
    fun initService() {
        super.createMockServer()
        service = createService(UserService::class.java)
    }

    @After
    fun tearDown() {
        super.stopServer()
    }

    @Test
    fun `send Feedback Test`() = runTest {
        // given
        enqueueResponse("/DefaultResponse.json")

        // when
        val token = "mockToken"
        val mockRequest = FeedbackRequest(
            content = "쿠링은 좋은 어플리케이션입니다."
        )
        val response = service.sendFeedback(token, mockRequest)
        mockWebServer.takeRequest()

        // then
        assertEquals(true, response.isSuccess)
        assertEquals("성공", response.resultMsg)
        assertEquals(null, response.data)
    }

    @Test
    fun `get UserData Test`() = runTest {
        //given
        enqueueResponse("/UserDataResponse.json")

        // when
        val token = "mockToken"

        val response = service.getUserData(token)
        mockWebServer.takeRequest()

        assertEquals("test@konkuk.ac.kr", response.data.email)
        assertEquals("test1234", response.data.nickname)
    }
}