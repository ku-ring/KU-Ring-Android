package com.ku_stacks.ku_ring.notice.api

import com.ku_stacks.ku_ring.notice.api.request.SubscribeRequest
import com.ku_stacks.ku_ring.notice.api.response.DepartmentNoticeListResponse
import com.ku_stacks.ku_ring.notice.api.response.NoticeListResponse
import com.ku_stacks.ku_ring.notice.api.response.SearchNoticeListResponse
import com.ku_stacks.ku_ring.notice.api.response.SubscribeListResponse
import com.ku_stacks.ku_ring.util.network.DefaultResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface NoticeService {
    @GET("v2/notices")
    fun fetchNoticeList(
        @Query("type") type: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Single<NoticeListResponse>

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
    fun fetchNotices(
        @Query("content") content: String,
    ): Single<SearchNoticeListResponse>

}