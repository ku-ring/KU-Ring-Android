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
        val response = noticeCommentClient.createComment(noticeId, parentCommentId, content)
        return when (response.resultCode) {
            422 -> Result.failure(IllegalArgumentException("댓글에 사용할 수 없는 단어가 포함되어 있습니다."))
            in 200 until 300 -> Result.success(Unit)
            else -> Result.failure(IllegalStateException("댓글 생성에 실패했습니다. 잠시 후 다시 시도해 주세요."))
        }
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