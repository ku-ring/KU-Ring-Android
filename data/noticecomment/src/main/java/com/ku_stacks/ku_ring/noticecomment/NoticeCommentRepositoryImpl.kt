package com.ku_stacks.ku_ring.noticecomment

import com.ku_stacks.ku_ring.domain.NoticeComment
import com.ku_stacks.ku_ring.domain.noticecomment.repository.NoticeCommentRepository
import com.ku_stacks.ku_ring.noticecomment.mapper.toDomain
import com.ku_stacks.ku_ring.remote.noticecomment.NoticeCommentClient
import com.ku_stacks.ku_ring.util.suspendRunCatching
import javax.inject.Inject

class NoticeCommentRepositoryImpl @Inject constructor(
    private val noticeCommentClient: NoticeCommentClient,
) : NoticeCommentRepository {
    override suspend fun createComment(
        noticeId: Int,
        parentCommentId: Int?,
        content: String
    ): Result<Unit> = suspendRunCatching {
        noticeCommentClient.createComment(noticeId, parentCommentId, content)
    }

    override suspend fun deleteComment(noticeId: Int, commentId: Int): Result<Unit> =
        suspendRunCatching {
            noticeCommentClient.deleteComment(noticeId, commentId)
        }

    override suspend fun getComment(
        noticeId: Int,
        cursor: Int?,
        size: Int
    ): Result<Pair<List<NoticeComment>, Boolean>> = suspendRunCatching {
        noticeCommentClient.getComment(noticeId, cursor, size).toDomain()
    }
}