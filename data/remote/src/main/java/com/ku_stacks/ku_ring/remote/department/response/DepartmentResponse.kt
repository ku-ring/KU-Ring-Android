package com.ku_stacks.ku_ring.remote.department.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DepartmentResponse(
    val name: String?,
    @SerialName("hostPrefix") val shortName: String?,
    val korName: String?,
)