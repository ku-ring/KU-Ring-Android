package com.ku_stacks.ku_ring.remote.place.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceBuildingResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("address") val address: String,
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double,
    @SerialName("imageUrl") val imageUrl: String? = null,
    // 전체 건물 목록에서만 내려오며, 서버에 우선순위가 없는 건물은 null이다.
    @SerialName("displayOrder") val displayOrder: Int? = null,
)
