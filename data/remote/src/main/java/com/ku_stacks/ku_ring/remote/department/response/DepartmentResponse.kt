package com.ku_stacks.ku_ring.remote.department.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DepartmentResponse(
    @SerialName("name") val name: String?,
    @SerialName("hostPrefix") val shortName: String?,
    @SerialName("korName") val korName: String?,
)