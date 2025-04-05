package com.ku_stacks.ku_ring.remote.noticecomment

import com.ku_stacks.ku_ring.remote.noticecomment.request.NoticeCommentCreateRequest
import com.ku_stacks.ku_ring.remote.noticecomment.request.NoticeCommentEditRequest
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import javax.inject.Inject

class NoticeCommentClient @Inject constructor(
    private val noticeCommentService: NoticeCommentService,
) {
    suspend fun createComment(
        noticeId: Int,
        parentNoticeId: Int?,
        content: String,
    ): DefaultResponse = noticeCommentService.createComment(
        noticeId = noticeId,
        request = NoticeCommentCreateRequest(parentNoticeId, content),
    )

    suspend fun editComment(
        noticeId: Int,
        commentId: Int,
        content: String,
    ): DefaultResponse = noticeCommentService.editComment(
        noticeId = noticeId,
        commentId = commentId,
        request = NoticeCommentEditRequest(content),
    )

    suspend fun deleteComment(
        noticeId: Int,
        commentId: Int,
    ): String = noticeCommentService.deleteComment(
        noticeId = noticeId,
        commentId = commentId,
    )
}