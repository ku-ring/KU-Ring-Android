package com.ku_stacks.ku_ring.main.campusmap.model

import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.PlaceFacility

internal fun List<PlaceFacility>.groupByCampusBuilding(): List<Place> =
    filter { it.building != null }
        .groupBy { requireNotNull(it.building).id }
        .values
        .map { facilities ->
            val building = requireNotNull(facilities.first().building)
            Place(
                id = building.id.toString(),
                name = building.name,
                category = BUILDING_CATEGORY_LABEL,
                address = building.address,
                latitude = building.latitude,
                longitude = building.longitude,
                priority = Place.Priority.HIGH,
                imageUrl = facilities.firstNotNullOfOrNull(PlaceFacility::imageUrl),
                facilities = facilities,
            )
        }

private const val BUILDING_CATEGORY_LABEL = "건물"
