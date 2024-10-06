package com.ku_stacks.ku_ring.remote.notice

import com.ku_stacks.ku_ring.remote.notice.request.SubscribeRequest
import com.ku_stacks.ku_ring.remote.notice.response.DepartmentNoticeListResponse
import com.ku_stacks.ku_ring.remote.notice.response.NoticeListResponse
import com.ku_stacks.ku_ring.remote.notice.response.SearchNoticeListResponse
import com.ku_stacks.ku_ring.remote.notice.response.SubscribeListResponse
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import retrofit2.http.*

interface NoticeService {
    // TODO: 공지 API와 학과 공지 API를 하나의 함수로 대응
    @GET("v2/notices")
    suspend fun fetchNoticeList(
        @Query("type") type: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): NoticeListResponse

    @GET("v2/users/subscriptions/categories")
    suspend fun fetchSubscribeList(
        @Header("User-Token") token: String
    ): SubscribeListResponse

    @POST("v2/users/subscriptions/categories")
    suspend fun saveSubscribeList(
        @Header("User-Token") token: String,
        @Body subscribeRequest: SubscribeRequest
    ): DefaultResponse

    @GET("v2/notices")
    suspend fun fetchDepartmentNoticeList(
        @Query("type") type: String,
        @Query("department") shortName: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("important") important: Boolean,
    ): DepartmentNoticeListResponse

    @GET("v2/notices/search")
    suspend fun fetchNotices(
        @Query("content") content: String,
    ): SearchNoticeListResponse
}
