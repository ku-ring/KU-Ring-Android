package com.ku_stacks.ku_ring.domain.noticecomment.util

import com.ku_stacks.ku_ring.domain.NoticeComment
import com.ku_stacks.ku_ring.domain.PlainNoticeComment

class NoticeCommentFactory {
    private var lastCommentId = 1

    fun create() = NoticeComment(
        comment = createPlainNoticeComment(),
        replies = emptyList(),
        hasNext = true,
    )

    private fun createPlainNoticeComment() = PlainNoticeComment(
        id = lastCommentId++,
        parentCommentId = 0,
        authorId = 123,
        authorName = "test",
        noticeId = NOTICE_ID,
        content = "Test comment $lastCommentId",
        postedDatetime = "test posted time",
        updatedDatetime = "test updated time",
    )

    companion object {
        const val NOTICE_ID = 123456
    }
}