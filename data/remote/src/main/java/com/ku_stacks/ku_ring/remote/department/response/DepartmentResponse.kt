package com.ku_stacks.ku_ring.remote.department.response

import com.google.gson.annotations.SerializedName

data class DepartmentResponse(
    val name: String?,
    @SerializedName("hostPrefix") val shortName: String?,
    val korName: String?,
)