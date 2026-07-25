package com.ku_stacks.ku_ring.remote.place.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceBuildingDetailResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("address") val address: String,
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("currentOperatingHours") val currentOperatingHours: PlaceOperatingHoursResponse? = null,
    @SerialName("campusPlaces") val campusPlaces: List<PlaceCampusPlaceResponse> = emptyList(),
)
