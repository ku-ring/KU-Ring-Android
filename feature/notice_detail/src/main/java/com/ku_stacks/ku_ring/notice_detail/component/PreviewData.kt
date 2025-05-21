package com.ku_stacks.ku_ring.notice_detail.component

import com.ku_stacks.ku_ring.domain.NoticeComment
import com.ku_stacks.ku_ring.domain.PlainNoticeComment

internal val fakePlainComment = PlainNoticeComment(
    id = 0,
    parentCommentId = 0,
    authorId = 0,
    authorName = "쿠링",
    noticeId = 0,
    content = "쿠링 댓글 내용".repeat(10),
    isMyComment = false,
    postedDatetime = "2025.03.23 20:27",
    updatedDatetime = "2025.03.23 20:27",
)
internal const val size = 10
internal val fakeComments: (Int) -> List<NoticeComment> = { size ->
    List(size) { index ->
        NoticeComment(
            comment = fakePlainComment.copy(
                id = index,
                authorName = "쿠링 $index",
                isMyComment = (index % 2 == 0),
            ),
            replies = List(2) {
                fakePlainComment.copy(
                    id = index * 10 + it,
                    parentCommentId = index,
                )
            },
            hasNext = (index == size - 1),
        )
    }
}