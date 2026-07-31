package com.ku_stacks.ku_ring.remote.place.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceOperatingHoursResponse(
    // period: "SEMESTER", "VACATION"
    @SerialName("period") val period: String,
    // dayGroup: "WEEKDAY", "WEEKEND"
    @SerialName("dayGroup") val dayGroup: String,
    // status: "OPEN_24_HOURS", "SCHEDULED", "UNKNOWN"
    @SerialName("status") val status: String,
    @SerialName("opensAt") val opensAt: String? = null,
    @SerialName("closesAt") val closesAt: String? = null,
    @SerialName("isCurrent") val isCurrent: Boolean,
)
