package com.ku_stacks.ku_ring.department.remote.request

import com.google.gson.annotations.SerializedName

data class DepartmentSubscribeRequest(
    @SerializedName(value = "departments")
    val departments: List<String>,
)
