package com.ku_stacks.ku_ring.remote.noticecomment

import com.ku_stacks.ku_ring.remote.noticecomment.request.NoticeCommentCreateRequest
import com.ku_stacks.ku_ring.remote.noticecomment.request.NoticeCommentEditRequest
import com.ku_stacks.ku_ring.remote.noticecomment.response.NoticeCommentResponse
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NoticeCommentService {
    @POST("v2/notices/{id}/comments")
    suspend fun createComment(
        @Path("id") noticeId: Int,
        @Body request: NoticeCommentCreateRequest,
    ): DefaultResponse

    @POST("v2/notices/{id}/comments/{commentId}")
    suspend fun editComment(
        @Path("id") noticeId: Int,
        @Path("commentId") commentId: Int,
        @Body request: NoticeCommentEditRequest,
    ): DefaultResponse

    @DELETE("v2/notices/{id}/comments/{commentId}")
    suspend fun deleteComment(
        @Path("id") noticeId: Int,
        @Path("commentId") commentId: Int,
    ): DefaultResponse

    @GET("v2/notices/{id}/comments")
    suspend fun getComment(
        @Path("id") noticeId: Int,
        @Query("cursor") cursor: String?,
        @Query("size") size: Int,
    ): NoticeCommentResponse
}