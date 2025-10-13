package com.ku_stacks.ku_ring.domain

data class PlainNoticeComment(
    val id: Int,
    val parentCommentId: Int?,
    val authorId: Int,
    val authorName: String,
    val noticeId: Int,
    val content: String,
    val isMyComment: Boolean,
    val postedDatetime: String,
    val updatedDatetime: String,
    // TODO: Field "destroyedAt" is not used now. Add when needed.
) {
    val shownPostedDatetime: String = run {
        val date = postedDatetime.substring(0..9).replace("-", ".")
        val time = postedDatetime.substring(11..15)
        "$date $time"
    }
}