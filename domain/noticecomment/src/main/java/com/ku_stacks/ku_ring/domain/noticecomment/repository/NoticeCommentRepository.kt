package com.ku_stacks.ku_ring.domain.noticecomment.repository

import com.ku_stacks.ku_ring.domain.NoticeComment

interface NoticeCommentRepository {
    suspend fun createComment(
        noticeId: Int,
        parentCommentId: Int?,
        content: String,
    ): Result<Unit>

    suspend fun deleteComment(
        noticeId: Int,
        commentId: Int,
    ): Result<Unit>

    suspend fun getComment(
        noticeId: Int,
        cursor: Int?,
        size: Int = PAGE_SIZE,
    ): Result<Pair<List<NoticeComment>, Boolean>>

    companion object {
        const val PAGE_SIZE = 20
    }
}