package com.ku_stacks.ku_ring.remote.noticecomment.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoticeCommentCreateRequest(
    /**
     * If null, create a common comment.
     * If not null, create a reply comment.
     */
    @SerialName("parentId")
    val parentId: Int?,
    /**
     * Content of the comment.
     */
    @SerialName("content")
    val content: String,
)
