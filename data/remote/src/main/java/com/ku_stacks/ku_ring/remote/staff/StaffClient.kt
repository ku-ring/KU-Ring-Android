package com.ku_stacks.ku_ring.remote.staff

import com.ku_stacks.ku_ring.remote.staff.response.SearchStaffListResponse
import javax.inject.Inject

class StaffClient @Inject constructor(private val staffService: StaffService) {

    suspend fun fetchStaffList(
        content: String
    ): SearchStaffListResponse = staffService.fetchStaffs(content)

}
