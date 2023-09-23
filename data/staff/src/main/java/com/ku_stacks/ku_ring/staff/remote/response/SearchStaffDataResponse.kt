package com.ku_stacks.ku_ring.staff.remote.response

import com.google.gson.annotations.SerializedName

data class SearchStaffDataResponse(
    @SerializedName("staffList") val staffList: List<SearchStaffResponse>?
)
