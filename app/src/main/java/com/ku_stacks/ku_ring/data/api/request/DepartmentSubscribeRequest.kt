package com.ku_stacks.ku_ring.data.api.request

import com.google.gson.annotations.SerializedName

data class DepartmentSubscribeRequest(
    @SerializedName(value = "departments")
    val departments: List<String>,
)
