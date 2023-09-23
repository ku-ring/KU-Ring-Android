package com.ku_stacks.ku_ring.staff.remote

import com.ku_stacks.ku_ring.staff.remote.response.SearchStaffListResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class StaffClient @Inject constructor(private val staffService: StaffService) {

    fun fetchStaffList(
        content: String
    ): Single<SearchStaffListResponse> = staffService.fetchStaffs(content)

}