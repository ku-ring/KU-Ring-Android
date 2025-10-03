package com.ku_stacks.ku_ring.remote.academicevent.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AcademicEventResponse (
    @SerialName("id") val id: Long,
    @SerialName("eventUid") val eventUid: String,
    @SerialName("summary") val summary: String,
    @SerialName("description") val description: String?,
    @SerialName("category") val category: String,
    @SerialName("startTime") val startTime: String,
    @SerialName("endTime") val endTime: String,
)
