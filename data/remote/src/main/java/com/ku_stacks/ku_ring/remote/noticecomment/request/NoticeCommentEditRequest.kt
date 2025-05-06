package com.ku_stacks.ku_ring.remote.noticecomment.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoticeCommentEditRequest(
    /**
     * Updated content of the comment.
     */
    @SerialName("content")
    val content: String,
)
