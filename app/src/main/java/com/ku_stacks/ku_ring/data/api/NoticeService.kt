package com.ku_stacks.ku_ring.data.api

import com.ku_stacks.ku_ring.data.api.request.SubscribeRequest
import com.ku_stacks.ku_ring.data.api.response.DefaultResponse
import com.ku_stacks.ku_ring.data.api.response.NoticeListResponse
import com.ku_stacks.ku_ring.data.api.response.SubscribeListResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NoticeService {
    @GET("notice?")
    fun fetchNoticeList(
        @Query("type") type: String,
        @Query("offset") offset: Int,
        @Query("max") max: Int,
    ): Single<NoticeListResponse>

    @GET("notice/subscribe")
    fun fetchSubscribeList(
        @Query("id") token: String
    ): Single<SubscribeListResponse>

    @POST("notice/subscribe")
    fun saveSubscribeList(
        @Body subscribeRequest: SubscribeRequest
    ): Single<DefaultResponse>
}