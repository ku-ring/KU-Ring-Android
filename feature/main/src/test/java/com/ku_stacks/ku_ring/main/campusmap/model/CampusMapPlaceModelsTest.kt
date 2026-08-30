package com.ku_stacks.ku_ring.main.campusmap.model

import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.PlaceBuilding
import com.ku_stacks.ku_ring.domain.PlaceFacility
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CampusMapPlaceModelsTest {

    @Test
    fun `facilities are grouped into high-priority marker places by API building`() {
        val managementBuilding = building(
            id = 2L,
            name = "경영관",
            latitude = 37.5443474,
            longitude = 127.0761119,
        )
        val studentUnionBuilding = building(
            id = 4L,
            name = "학생회관",
            latitude = 37.541823,
            longitude = 127.0779007,
        )
        val managementCafe = facility(
            id = 201L,
            name = "카페 레스티오",
            building = managementBuilding,
        )
        val managementStore = facility(
            id = 202L,
            name = "CU 경영관점",
            building = managementBuilding,
            imageUrl = "https://example.com/cu.png",
        )
        val studentUnionCafe = facility(
            id = 405L,
            name = "1847 샐러드카페",
            building = studentUnionBuilding,
        )

        val places = listOf(
            managementCafe,
            managementStore,
            studentUnionCafe,
        ).groupByCampusBuilding()

        assertEquals(listOf("2", "4"), places.map { it.id })
        assertEquals(listOf("경영관", "학생회관"), places.map { it.name })
        assertTrue(places.all { it.priority == Place.Priority.HIGH })
        assertTrue(places.all { it.category == "건물" })
        assertEquals(listOf(managementCafe, managementStore), places.first().facilities)
        assertEquals("https://example.com/cu.png", places.first().imageUrl)
        assertEquals(managementBuilding.latitude, places.first().latitude)
        assertEquals(managementBuilding.longitude, places.first().longitude)
    }

    @Test
    fun `facilities without an embedded building are excluded from map grouping`() {
        val validFacility = facility(
            id = 201L,
            name = "카페 레스티오",
            building = building(id = 2L, name = "경영관"),
        )
        val detailFacilityWithoutBuilding = facility(
            id = 202L,
            name = "CU 경영관점",
            building = null,
        )

        val places = listOf(validFacility, detailFacilityWithoutBuilding).groupByCampusBuilding()

        assertEquals(1, places.size)
        assertEquals(listOf(validFacility), places.single().facilities)
    }

    @Test
    fun `empty facility response produces no marker places`() {
        assertTrue(emptyList<PlaceFacility>().groupByCampusBuilding().isEmpty())
    }

    @Test
    fun `search places merge facilities into one marker per parent building`() {
        val building = building(id = 2L, name = "경영관")
        val buildingPlace = Place(
            id = "2",
            name = "경영관",
            category = "건물",
            address = building.address,
            latitude = building.latitude,
            longitude = building.longitude,
            priority = Place.Priority.HIGH,
        )
        val cafe = facility(id = 201L, name = "카페 레스티오", building = building)
        val printer = facility(id = 202L, name = "경영관 복사실", building = building)

        val places = mergeCampusMapSearchPlaces(
            buildings = listOf(buildingPlace),
            campusPlaces = listOf(cafe, printer),
        )

        assertEquals(1, places.size)
        assertEquals("2", places.single().id)
        assertEquals(listOf(cafe, printer), places.single().facilities)
    }

    private fun building(
        id: Long,
        name: String,
        latitude: Double = 37.542366,
        longitude: Double = 127.076846,
    ) = PlaceBuilding(
        id = id,
        name = name,
        address = "서울특별시 광진구 능동로 120",
        latitude = latitude,
        longitude = longitude,
    )

    private fun facility(
        id: Long,
        name: String,
        building: PlaceBuilding?,
        imageUrl: String? = null,
    ) = PlaceFacility(
        id = id,
        name = name,
        category = "cafe",
        categoryKor = "카페",
        imageUrl = imageUrl,
        building = building,
    )
}
