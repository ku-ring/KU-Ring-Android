package com.ku_stacks.ku_ring.remote.place.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceOperatingHoursResponse(
    @SerialName("period") val period: String,
    @SerialName("dayGroup") val dayGroup: String,
    @SerialName("status") val status: String,
    @SerialName("opensAt") val opensAt: String? = null,
    @SerialName("closesAt") val closesAt: String? = null,
)
