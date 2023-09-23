package com.ku_stacks.ku_ring.staff.remote

import com.ku_stacks.ku_ring.staff.remote.response.SearchStaffListResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface StaffService {
    @GET("v2/staffs/search")
    fun fetchStaffs(
        @Query("content") content: String,
    ): Single<SearchStaffListResponse>
}