package com.ku_stacks.ku_ring.remote.staff

import com.ku_stacks.ku_ring.remote.staff.response.SearchStaffDataResponse
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import javax.inject.Inject

class StaffClient @Inject constructor(private val staffService: StaffService) {

    suspend fun fetchStaffList(
        content: String
    ): DefaultResponse<SearchStaffDataResponse> = staffService.fetchStaffs(content)

}
