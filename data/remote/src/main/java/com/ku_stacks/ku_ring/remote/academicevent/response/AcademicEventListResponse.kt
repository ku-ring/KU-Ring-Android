package com.ku_stacks.ku_ring.remote.academicevent.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AcademicEventListResponse(
    @SerialName("code") val code: Int,
    @SerialName("message") val message: String,
    @SerialName("data") val data: List<AcademicEventResponse>,
)
