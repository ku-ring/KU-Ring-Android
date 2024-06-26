package com.ku_stacks.ku_ring.remote.staff.response

import com.google.gson.annotations.SerializedName

data class SearchStaffResponse(
    @SerializedName("name") val name: String,
    @SerializedName("major") val major: String,
    @SerializedName("lab") val lab: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("email") val email: String,
    @SerializedName("deptName") val deptName: String,
    @SerializedName("collegeName") val collegeName: String,
)