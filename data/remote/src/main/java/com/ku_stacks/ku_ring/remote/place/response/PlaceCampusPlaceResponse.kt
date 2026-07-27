package com.ku_stacks.ku_ring.remote.place.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceCampusPlaceResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("category") val category: String,
    @SerialName("categoryKorName") val categoryKorName: String,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("locationType") val locationType: String,
    @SerialName("floor") val floor: String,
    @SerialName("locationDetail") val locationDetail: String? = null,
    @SerialName("quantity") val quantity: Int? = null,
    @SerialName("currentOperatingHours") val currentOperatingHours: PlaceOperatingHoursResponse,
    @SerialName("externalUrl") val externalUrl: String? = null,
    @SerialName("building") val building: PlaceBuildingResponse,
)
