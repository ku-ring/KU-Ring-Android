package com.ku_stacks.ku_ring.remote.place.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceCategoryResponse(
    @SerialName("name") val name: String,
    @SerialName("korName") val korName: String,
    @SerialName("displayOrder") val displayOrder: Int,
)
