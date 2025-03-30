package com.ku_stacks.ku_ring.domain

data class NoticeComment(
    val comment: PlainNoticeComment,
    val replies: List<PlainNoticeComment>,
    val hasNext: Boolean,
)