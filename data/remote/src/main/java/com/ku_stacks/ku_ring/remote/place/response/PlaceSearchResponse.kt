package com.ku_stacks.ku_ring.remote.place.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceSearchResponse(
    @SerialName("buildings") val buildings: List<PlaceBuildingResponse> = emptyList(),
    @SerialName("campusPlaces") val campusPlaces: List<PlaceCampusPlaceResponse> = emptyList(),
)
