package com.ku_stacks.ku_ring.remote.notice

import com.ku_stacks.ku_ring.remote.notice.request.SubscribeRequest
import com.ku_stacks.ku_ring.remote.notice.response.CategoryResponse
import com.ku_stacks.ku_ring.remote.notice.response.DepartmentNoticeResponse
import com.ku_stacks.ku_ring.remote.notice.response.NoticeResponse
import com.ku_stacks.ku_ring.remote.notice.response.SearchNoticeDataResponse
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface NoticeService {
    // TODO: 공지 API와 학과 공지 API를 하나의 함수로 대응
    @GET("v2/notices")
    suspend fun fetchNoticeList(
        @Query("type") type: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): DefaultResponse<List<NoticeResponse>>

    @GET("v2/users/subscriptions/categories")
    suspend fun fetchSubscribeList(
        @Header("User-Token") token: String
    ): DefaultResponse<List<CategoryResponse>>

    @POST("v2/users/subscriptions/categories")
    suspend fun saveSubscribeList(
        @Header("User-Token") token: String,
        @Body subscribeRequest: SubscribeRequest
    ): DefaultResponse<Unit>

    @GET("v2/notices")
    suspend fun fetchDepartmentNoticeList(
        @Query("type") type: String,
        @Query("department") shortName: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("important") important: Boolean,
    ): DefaultResponse<List<DepartmentNoticeResponse>>

    @GET("v2/notices/search")
    suspend fun fetchNotices(
        @Query("content") content: String,
    ): DefaultResponse<SearchNoticeDataResponse>
}
