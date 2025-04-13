package com.ku_stacks.ku_ring.remote

import com.ku_stacks.ku_ring.remote.noticecomment.NoticeCommentService
import com.ku_stacks.ku_ring.remote.noticecomment.request.NoticeCommentCreateRequest
import com.ku_stacks.ku_ring.remote.noticecomment.request.NoticeCommentEditRequest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NoticeCommentServiceTest : ApiAbstract<NoticeCommentService>() {

    private lateinit var service: NoticeCommentService

    @Before
    fun initService() {
        super.createMockServer()
        service = createService(NoticeCommentService::class.java)
    }

    @After
    fun tearDown() {
        super.stopServer()
    }

    @Test
    fun `add notice comment succeeded`() = runTest {
        // given
        enqueueResponse("/AddNoticeCommentResponse.json")

        // when
        val request = NoticeCommentCreateRequest(
            parentId = null,
            content = "test comment",
        )
        val response = service.createComment(123, request)
        mockWebServer.takeRequest()

        // then
        assertEquals(true, response.isSuccess)
        assertEquals(200, response.resultCode)
    }

    @Test
    fun `get notice comment succeeded`() = runTest {
        // given
        enqueueResponse("/GetNoticeCommentResponse.json")

        // when
        val response = service.getComment(168950, null, 10)
        mockWebServer.takeRequest()

        assertEquals(200, response.code)
        assertEquals(4, response.data.comments.size)

        response.data.comments.forEach { comment ->
            // empty array is
            comment.replies?.forEach { reply ->
                assertEquals(comment.comment.commentId, reply.parentCommentId)
            }
        }
    }

    @Test
    fun `edit notice comment succeeded`() = runTest {
        // given
        enqueueResponse("/EditNoticeCommentResponse.json")

        // when
        val request = NoticeCommentEditRequest(
            content = "new comment",
        )
        val response = service.editComment(123, 1, request)
        mockWebServer.takeRequest()

        // then
        assertEquals(true, response.isSuccess)
        assertEquals(200, response.resultCode)
    }
}