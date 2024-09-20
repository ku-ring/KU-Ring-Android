package com.ku_stacks.ku_ring.remote.notice

import com.ku_stacks.ku_ring.remote.notice.request.SubscribeRequest
import com.ku_stacks.ku_ring.remote.notice.response.DepartmentNoticeListResponse
import com.ku_stacks.ku_ring.remote.notice.response.NoticeListResponse
import com.ku_stacks.ku_ring.remote.notice.response.SearchNoticeListResponse
import com.ku_stacks.ku_ring.remote.notice.response.SubscribeListResponse
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface NoticeService {
    @GET("v2/notices")
    suspend fun fetchNoticeList(
        @Query("type") type: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): NoticeListResponse

    @GET("v2/users/subscriptions/categories")
    fun fetchSubscribeList(
        @Header("User-Token") token: String
    ): Single<SubscribeListResponse>

    @POST("v2/users/subscriptions/categories")
    fun saveSubscribeList(
        @Header("User-Token") token: String,
        @Body subscribeRequest: SubscribeRequest
    ): Single<DefaultResponse>

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
