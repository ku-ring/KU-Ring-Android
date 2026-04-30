package com.ku_stacks.ku_ring.remote.staff

import com.ku_stacks.ku_ring.remote.staff.response.SearchStaffDataResponse
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface StaffService {
    @GET("v2/staffs/search")
    suspend fun fetchStaffs(
        @Query("content") content: String,
    ): DefaultResponse<SearchStaffDataResponse>
}
