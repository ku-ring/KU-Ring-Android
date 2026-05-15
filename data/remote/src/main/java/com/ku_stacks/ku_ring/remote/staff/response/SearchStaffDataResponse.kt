package com.ku_stacks.ku_ring.remote.staff.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchStaffDataResponse(
    @SerialName("staffList") val staffList: List<SearchStaffResponse>?
)
