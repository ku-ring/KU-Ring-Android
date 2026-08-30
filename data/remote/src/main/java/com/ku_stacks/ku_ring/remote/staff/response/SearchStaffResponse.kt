package com.ku_stacks.ku_ring.remote.staff.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchStaffResponse(
    @SerialName("name") val name: String,
    @SerialName("major") val major: String,
    @SerialName("lab") val lab: String,
    @SerialName("phone") val phone: String,
    @SerialName("email") val email: String,
    @SerialName("deptName") val deptName: String,
    @SerialName("collegeName") val collegeName: String,
)