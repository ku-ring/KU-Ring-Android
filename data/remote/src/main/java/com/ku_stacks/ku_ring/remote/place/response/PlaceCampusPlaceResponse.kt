package com.ku_stacks.ku_ring.remote.place.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceCampusPlaceResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    // category: "cafe", "restaurant", "printer", "smoking_booth", "convenience_store", "lounge", "kcube"
    @SerialName("category") val category: String,
    @SerialName("categoryKorName") val categoryKorName: String,
    @SerialName("imageUrl") val imageUrl: String? = null,
    // locationType: "INDOOR", "OUTDOOR"
    @SerialName("locationType") val locationType: String,
    @SerialName("floor") val floor: String? = null,
    @SerialName("locationDetail") val locationDetail: String? = null,
    @SerialName("quantity") val quantity: Int? = null,
    @SerialName("operatingHours") val operatingHours: List<PlaceOperatingHoursResponse>,
    @SerialName("externalUrl") val externalUrl: String? = null,
    // /buildings/{id} 상세 응답의 campusPlaces 항목에는 building이 내려오지 않는다
    @SerialName("building") val building: PlaceBuildingResponse? = null,
)
