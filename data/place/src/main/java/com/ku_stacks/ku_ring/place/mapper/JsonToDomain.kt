package com.ku_stacks.ku_ring.place.mapper

import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.PlaceFacility
import com.ku_stacks.ku_ring.domain.PlaceOperationHours
import com.ku_stacks.ku_ring.domain.Place.Priority
import com.ku_stacks.ku_ring.place.model.JsonPlace
import com.ku_stacks.ku_ring.place.model.JsonPlaceFacility
import com.ku_stacks.ku_ring.place.model.JsonPlaceOperationHours

internal fun JsonPlace.toDomain() = Place(
    id = id,
    name = name,
    category = category,
    address = address,
    latitude = latitude,
    longitude = longitude,
    priority = Priority.from(priority),
    number = number,
    iconUrl = iconUrl,
    phone = phone,
    data = data,
    imageUrl = imageUrl,
    operationHours = operationHours?.toDomain(),
    facilities = facilities?.map { it.toDomain() } ?: places.toFacilities(),
)

private fun Map<String, List<String>>.toFacilities(): List<PlaceFacility> =
    flatMap { (category, names) ->
        names.map { name ->
            PlaceFacility(
                name = name,
                category = category,
            )
        }
    }

private fun JsonPlaceFacility.toDomain() = PlaceFacility(
    name = name,
    category = category,
    location = location,
    operationHours = operationHours?.toDomain(),
)

private fun JsonPlaceOperationHours.toDomain() = PlaceOperationHours(
    current = current,
    semester = semester,
    vacation = vacation,
)
