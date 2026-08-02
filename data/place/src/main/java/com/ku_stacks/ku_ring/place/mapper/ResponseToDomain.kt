package com.ku_stacks.ku_ring.place.mapper

import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.PlaceBuilding
import com.ku_stacks.ku_ring.domain.PlaceCategory
import com.ku_stacks.ku_ring.domain.PlaceFacility
import com.ku_stacks.ku_ring.domain.PlaceOperationHours
import com.ku_stacks.ku_ring.remote.place.response.PlaceBuildingDetailResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceBuildingResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceCampusPlaceResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceCategoryResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceOperatingHoursResponse

internal fun PlaceBuildingResponse.toDomain() = Place(
    id = id.toString(),
    name = name,
    category = "",
    address = address,
    latitude = latitude,
    longitude = longitude,
    priority = Place.Priority.MIDDLE,
)

internal fun PlaceBuildingDetailResponse.toDomain() = Place(
    id = id.toString(),
    name = name,
    category = "",
    address = address,
    latitude = latitude,
    longitude = longitude,
    priority = Place.Priority.MIDDLE,
    imageUrl = imageUrl,
    operationHours = operatingHours.toDomain(),
    facilities = campusPlaces.map { it.toDomain() },
)

internal fun PlaceCampusPlaceResponse.toDomain() = PlaceFacility(
    name = name,
    category = category,
    categoryKor = categoryKorName,
    location = listOfNotNull(floor, locationDetail).joinToString(" ").ifBlank { null },
    operationHours = operatingHours.toDomain(),
    id = id,
    imageUrl = imageUrl,
    quantity = quantity,
    externalUrl = externalUrl,
    building = building?.let {
        PlaceBuilding(
            id = it.id,
            name = it.name,
            address = it.address,
            latitude = it.latitude,
            longitude = it.longitude,
        )
    },
)

// 서버는 (SEMESTER/VACATION) x (WEEKDAY/WEEKEND) 4개 조합을 배열로 내려준다.
// isCurrent=true인 항목을 current로, 요일군별 텍스트를 합쳐 semester/vacation을 만든다.
private fun List<PlaceOperatingHoursResponse>.toDomain() = PlaceOperationHours(
    current = firstOrNull { it.isCurrent }?.toDisplayText(),
    semester = toPeriodDisplayText(period = "SEMESTER"),
    vacation = toPeriodDisplayText(period = "VACATION"),
)

private fun List<PlaceOperatingHoursResponse>.toPeriodDisplayText(period: String): String? {
    val weekday = firstOrNull { it.period == period && it.dayGroup == "WEEKDAY" }?.toDisplayText()
    val weekend = firstOrNull { it.period == period && it.dayGroup == "WEEKEND" }?.toDisplayText()
    return when {
        weekday == null && weekend == null -> null
        weekday == weekend -> weekday
        else -> listOfNotNull(
            weekday?.let { "평일 $it" },
            weekend?.let { "주말 $it" },
        ).joinToString(" / ")
    }
}

private fun PlaceOperatingHoursResponse.toDisplayText(): String? = when (status) {
    "OPEN_24_HOURS" -> "24시간 운영"
    "SCHEDULED" -> "$opensAt - $closesAt"
    else -> null // UNKNOWN: 운영시간 정보 없음
}

internal fun PlaceCategoryResponse.toDomain() = PlaceCategory(
    name = name,
    korName = korName,
    displayOrder = displayOrder,
)
