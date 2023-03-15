package com.ku_stacks.ku_ring.data.api

import com.ku_stacks.ku_ring.data.api.response.SearchNoticeListResponse
import com.ku_stacks.ku_ring.data.api.response.SearchStaffListResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("v2/notices/search")
    fun fetchNotices(
        @Query("content") content: String,
    ): Single<SearchNoticeListResponse>

    @GET("v2/staffs/search")
    fun fetchStaffs(
        @Query("content") content: String,
    ): Single<SearchStaffListResponse>
}