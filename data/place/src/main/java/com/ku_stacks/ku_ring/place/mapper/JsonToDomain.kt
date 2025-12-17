package com.ku_stacks.ku_ring.place.mapper

import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.place.model.JsonPlace

internal fun JsonPlace.toDomain() = Place(
    id = id,
    name = name,
    category = category,
    address = address,
    latitude = latitude,
    longitude = longitude,
)