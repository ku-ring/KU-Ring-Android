package com.ku_stacks.ku_ring.remote.noticecomment.request

import com.google.gson.annotations.SerializedName

data class NoticeCommentEditRequest(
    /**
     * Updated content of the comment.
     */
    @SerializedName("content")
    val content: String,
)
