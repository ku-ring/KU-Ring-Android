package com.ku_stacks.ku_ring.remote.staff

import com.ku_stacks.ku_ring.remote.staff.response.SearchStaffListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface StaffService {
    @GET("v2/staffs/search")
    suspend fun fetchStaffs(
        @Query("content") content: String,
    ): SearchStaffListResponse
}
