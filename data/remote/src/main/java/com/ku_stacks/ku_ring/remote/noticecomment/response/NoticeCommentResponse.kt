package com.ku_stacks.ku_ring.remote.noticecomment.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoticeCommentResponse(
    @SerialName("code") val code: Int,
    @SerialName("message") val message: String,
    @SerialName("data") val data: NoticeCommentResponseData,
) {
    @Serializable
    data class NoticeCommentResponseData(
        @SerialName("comments") val comments: List<NoticeCommentWithReplyResponse>,
        @SerialName("endCursor") val lastCommentId: String?,
        @SerialName("hasNext") val hasNext: Boolean,
    )

    @Serializable
    data class NoticeCommentWithReplyResponse(
        @SerialName("comment") val comment: PlainNoticeCommentResponse,
        @SerialName("subComments") val replies: List<PlainNoticeCommentResponse>?,
    )

    @Serializable
    data class PlainNoticeCommentResponse(
        @SerialName("id") val commentId: Int,
        @SerialName("parentId") val parentCommentId: Int?,
        @SerialName("userId") val authorId: Int,
        @SerialName("nickName") val authorName: String,
        @SerialName("noticeId") val noticeId: Int,
        @SerialName("content") val content: String,
        @SerialName("createdAt") val createdAt: String,
        @SerialName("updatedAt") val updatedAt: String,
        // TODO: Field "destroyedAt" is not used now. Add when needed.
    )
}
