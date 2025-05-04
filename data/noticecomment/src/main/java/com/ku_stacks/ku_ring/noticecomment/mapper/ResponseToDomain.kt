package com.ku_stacks.ku_ring.noticecomment.mapper

import com.ku_stacks.ku_ring.domain.NoticeComment
import com.ku_stacks.ku_ring.domain.PlainNoticeComment
import com.ku_stacks.ku_ring.remote.noticecomment.response.NoticeCommentResponse

internal fun NoticeCommentResponse.toDomain(): Pair<List<NoticeComment>, Boolean> {
    val comments = data.comments.mapIndexed { index, noticeCommentWithReplyResponse ->
        NoticeComment(
            comment = noticeCommentWithReplyResponse.comment.toDomain(),
            replies = noticeCommentWithReplyResponse.replies?.map { it.toDomain() } ?: emptyList(),
            hasNext = if (index == data.comments.lastIndex) true else data.hasNext,
        )
    }
    return Pair(comments, data.hasNext)
}

internal fun NoticeCommentResponse.PlainNoticeCommentResponse.toDomain() = PlainNoticeComment(
    id = commentId,
    parentCommentId = parentCommentId,
    authorId = authorId,
    authorName = authorName,
    noticeId = noticeId,
    content = content,
    postedDatetime = createdAt,
    updatedDatetime = updatedAt,
)