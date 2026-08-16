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

internal fun mergeCampusMapSearchPlaces(
    buildings: List<Place>,
    campusPlaces: List<PlaceFacility>,
    referenceBuildings: List<Place> = emptyList(),
): List<Place> {
    val referenceBuildingsById = referenceBuildings.associateBy(Place::id)
    val facilityPlacesByBuildingId = campusPlaces
        .groupByCampusBuilding()
        .associateBy(Place::id)
    val buildingIds = buildings.mapTo(mutableSetOf(), Place::id)

    return buildList {
        buildings.forEach { building ->
            val facilityPlace = facilityPlacesByBuildingId[building.id]
            val referenceBuilding = referenceBuildingsById[building.id]
            add(
                building.copy(
                    priority = referenceBuilding?.priority ?: building.priority,
                    imageUrl = building.imageUrl
                        ?: facilityPlace?.imageUrl
                        ?: referenceBuilding?.imageUrl,
                    operationHours = building.operationHours
                        ?: referenceBuilding?.operationHours,
                    facilities = facilityPlace?.facilities ?: building.facilities,
                ),
            )
        }
        facilityPlacesByBuildingId.values
            .filterNot { place -> place.id in buildingIds }
            .forEach { facilityPlace ->
                val referenceBuilding = referenceBuildingsById[facilityPlace.id]
                add(
                    referenceBuilding?.copy(
                        imageUrl = referenceBuilding.imageUrl ?: facilityPlace.imageUrl,
                        facilities = facilityPlace.facilities,
                    ) ?: facilityPlace,
                )
            }
    }
}

private const val BUILDING_CATEGORY_LABEL = "건물"
