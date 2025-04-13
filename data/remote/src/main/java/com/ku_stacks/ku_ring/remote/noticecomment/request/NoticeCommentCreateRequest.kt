package com.ku_stacks.ku_ring.remote.noticecomment.request

import com.google.gson.annotations.SerializedName

data class NoticeCommentCreateRequest(
    /**
     * If null, create a common comment.
     * If not null, create a reply comment.
     */
    @SerializedName("parentId")
    val parentId: Int?,
    /**
     * Content of the comment.
     */
    @SerializedName("content")
    val content: String,
)
