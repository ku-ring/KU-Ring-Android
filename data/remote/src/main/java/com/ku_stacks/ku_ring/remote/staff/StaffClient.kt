package com.ku_stacks.ku_ring.remote.staff

import com.ku_stacks.ku_ring.remote.staff.response.SearchStaffListResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class StaffClient @Inject constructor(private val staffService: StaffService) {

    fun fetchStaffList(
        content: String
    ): Single<SearchStaffListResponse> = staffService.fetchStaffs(content)

}