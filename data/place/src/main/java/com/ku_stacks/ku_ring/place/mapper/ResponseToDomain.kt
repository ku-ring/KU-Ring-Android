package com.ku_stacks.ku_ring.place.mapper

import com.ku_stacks.ku_ring.domain.Place
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
    operationHours = currentOperatingHours?.toDomain(),
    facilities = campusPlaces.map { it.toDomain() },
)

internal fun PlaceCampusPlaceResponse.toDomain() = PlaceFacility(
    name = name,
    category = categoryKorName,
    location = listOfNotNull(floor, locationDetail).joinToString(" ").ifBlank { null },
    operationHours = currentOperatingHours?.toDomain(),
    id = id,
    imageUrl = imageUrl,
    quantity = quantity,
    externalUrl = externalUrl,
)

internal fun PlaceOperatingHoursResponse.toDomain() = PlaceOperationHours(
    current = toDisplayText(),
)

private fun PlaceOperatingHoursResponse.toDisplayText(): String = when (status) {
    "OPEN_24_HOURS" -> "24시간 운영"
    "CLOSED" -> "휴무"
    else -> if (opensAt != null && closesAt != null) "$opensAt - $closesAt" else "-"
}

internal fun PlaceCategoryResponse.toDomain() = PlaceCategory(
    name = name,
    korName = korName,
    displayOrder = displayOrder,
)
