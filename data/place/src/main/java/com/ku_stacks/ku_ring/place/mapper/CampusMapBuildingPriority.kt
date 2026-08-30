package com.ku_stacks.ku_ring.place.mapper

import com.ku_stacks.ku_ring.domain.Place

internal fun Int?.toCampusMapBuildingPriority(): Place.Priority = when (this) {
    1 -> Place.Priority.HIGH
    2 -> Place.Priority.MIDDLE
    3 -> Place.Priority.LOW
    else -> Place.Priority.LOW
}
