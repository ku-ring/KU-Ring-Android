package com.ku_stacks.ku_ring.remote.place.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceCampusPlaceListResponse(
    @SerialName("campusPlaces") val campusPlaces: List<PlaceCampusPlaceResponse>,
)
