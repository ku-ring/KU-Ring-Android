package com.ku_stacks.ku_ring.department.remote.response

import com.google.gson.annotations.SerializedName

data class DepartmentResponse(
    val name: String?,
    @SerializedName("hostPrefix") val shortName: String?,
    val korName: String?,
)