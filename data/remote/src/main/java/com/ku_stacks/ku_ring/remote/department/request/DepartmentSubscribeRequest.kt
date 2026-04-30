package com.ku_stacks.ku_ring.remote.department.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DepartmentSubscribeRequest(
    @SerialName(value = "departments")
    val departments: List<String>,
)
