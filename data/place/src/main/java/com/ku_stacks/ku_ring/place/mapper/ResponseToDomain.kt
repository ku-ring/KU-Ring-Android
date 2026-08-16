package com.ku_stacks.ku_ring.place.mapper

import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.PlaceBuilding
import com.ku_stacks.ku_ring.domain.PlaceCategory
import com.ku_stacks.ku_ring.domain.PlaceFacility
import com.ku_stacks.ku_ring.domain.PlaceOperationHours
import com.ku_stacks.ku_ring.domain.PlaceSearchResult
import com.ku_stacks.ku_ring.remote.place.response.PlaceBuildingDetailResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceBuildingResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceCampusPlaceResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceCategoryResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceOperatingHoursResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceSearchResponse

internal fun PlaceBuildingResponse.toDomain() = Place(
    id = id.toString(),
    name = name,
    category = BUILDING_CATEGORY,
    address = address,
    latitude = latitude,
    longitude = longitude,
    priority = displayOrder.toCampusMapBuildingPriority(buildingName = name),
)

internal fun PlaceSearchResponse.toDomain() = PlaceSearchResult(
    buildings = buildings.map { it.toDomain() },
    campusPlaces = campusPlaces.map { it.toDomain() },
)

internal fun PlaceBuildingDetailResponse.toDomain() = Place(
    id = id.toString(),
    name = name,
    category = BUILDING_CATEGORY,
    address = address,
    latitude = latitude,
    longitude = longitude,
    priority = name.toCampusMapBuildingPriority(),
    imageUrl = imageUrl,
    operationHours = operatingHours.toDomain(),
    facilities = campusPlaces.map { it.toDomain() },
)

internal fun PlaceCampusPlaceResponse.toDomain() = PlaceFacility(
    name = name,
    category = category,
    categoryKor = categoryKorName,
    location = locationDetail.takeUnless { it.isNullOrBlank() }
        ?: floor.takeUnless { it.isNullOrBlank() },
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
// isCurrent=true인 항목과 학기/방학·주중/주말 항목을 각각 도메인 필드에 보존한다.
private fun List<PlaceOperatingHoursResponse>.toDomain() = PlaceOperationHours(
    current = firstOrNull { it.isCurrent }?.toDisplayText(),
    semesterWeekday = findDisplayText(period = "SEMESTER", dayGroup = "WEEKDAY"),
    semesterWeekend = findDisplayText(period = "SEMESTER", dayGroup = "WEEKEND"),
    vacationWeekday = findDisplayText(period = "VACATION", dayGroup = "WEEKDAY"),
    vacationWeekend = findDisplayText(period = "VACATION", dayGroup = "WEEKEND"),
)

private fun List<PlaceOperatingHoursResponse>.findDisplayText(
    period: String,
    dayGroup: String,
): String? = firstOrNull { response ->
    response.period == period && response.dayGroup == dayGroup
}?.toDisplayText()

private fun PlaceOperatingHoursResponse.toDisplayText(): String? = when (status) {
    "OPEN_24_HOURS" -> "24시간 운영"
    "SCHEDULED" -> if (!opensAt.isNullOrBlank() && !closesAt.isNullOrBlank()) {
        "$opensAt ~ $closesAt"
    } else {
        null
    }
    else -> null // UNKNOWN
}

internal fun PlaceCategoryResponse.toDomain() = PlaceCategory(
    name = name,
    korName = korName,
    displayOrder = displayOrder,
)

private const val BUILDING_CATEGORY = "건물"
