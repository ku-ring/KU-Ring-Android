package com.ku_stacks.ku_ring.data.api.response

import com.google.gson.annotations.SerializedName

data class SearchStaffDataResponse(
    @SerializedName("staffList") val staffList: List<SearchStaffResponse>
)
