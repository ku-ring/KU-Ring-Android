package com.ku_stacks.ku_ring.place

import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.place.repository.PlaceRepository
import com.ku_stacks.ku_ring.place.repository.PlaceRepositoryImpl
import com.ku_stacks.ku_ring.remote.place.PlaceClient
import com.ku_stacks.ku_ring.remote.place.response.PlaceBuildingDetailResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceBuildingListResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceBuildingResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceCampusPlaceListResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceCampusPlaceResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceCategoryListResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceCategoryResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceOperatingHoursResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceSearchResponse
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class PlaceRepositoryTest {
    private lateinit var repository: PlaceRepository
    private lateinit var placeClient: PlaceClient

    @Before
    fun setUp() {
        placeClient = mock()
        repository = PlaceRepositoryImpl(placeClient)
    }

    @Test
    fun `building detail maps metadata facilities and operating hours`() = runTest {
        val detail = PlaceBuildingDetailResponse(
            id = 2L,
            name = "경영관",
            address = "서울특별시 광진구 능동로 120",
            latitude = 37.5443474,
            longitude = 127.0761119,
            imageUrl = "https://example.com/building.png",
            operatingHours = listOf(
                operatingHours(
                    period = "SEMESTER",
                    dayGroup = "WEEKDAY",
                    status = "SCHEDULED",
                    opensAt = "08:00",
                    closesAt = "22:00",
                    isCurrent = true,
                ),
                operatingHours(
                    period = "SEMESTER",
                    dayGroup = "WEEKEND",
                    status = "UNKNOWN",
                ),
                operatingHours(
                    period = "VACATION",
                    dayGroup = "WEEKDAY",
                    status = "OPEN_24_HOURS",
                ),
                operatingHours(
                    period = "VACATION",
                    dayGroup = "WEEKEND",
                    status = "OPEN_24_HOURS",
                ),
            ),
            campusPlaces = listOf(
                campusPlace(
                    id = 201L,
                    name = "카페 레스티오",
                    floor = "1F",
                    locationDetail = "경영관 1층",
                    operatingHours = listOf(
                        operatingHours(
                            period = "SEMESTER",
                            dayGroup = "WEEKDAY",
                            status = "SCHEDULED",
                            isCurrent = true,
                        ),
                    ),
                ),
            ),
        )
        whenever(placeClient.fetchBuildingDetail(2L)).thenReturn(success(detail))

        val place = repository.getPlaceBuildingDetail(2L).getOrThrow()

        assertEquals("2", place.id)
        assertEquals("경영관", place.name)
        assertEquals("건물", place.category)
        assertEquals(Place.Priority.HIGH, place.priority)
        assertEquals("https://example.com/building.png", place.imageUrl)
        assertEquals("08:00 ~ 22:00", place.operationHours?.current)
        assertEquals("08:00 ~ 22:00", place.operationHours?.semesterWeekday)
        assertNull(place.operationHours?.semesterWeekend)
        assertEquals("24시간 운영", place.operationHours?.vacationWeekday)
        assertEquals("24시간 운영", place.operationHours?.vacationWeekend)

        val facility = place.facilities.single()
        assertEquals(201L, facility.id)
        assertEquals("cafe", facility.category)
        assertEquals("카페", facility.categoryKor)
        assertEquals("경영관 1층", facility.location)
        assertNull(facility.operationHours?.current)
        assertNull(facility.operationHours?.semesterWeekday)
        assertNull(facility.operationHours?.semesterWeekend)
        assertNull(facility.operationHours?.vacationWeekday)
        assertNull(facility.operationHours?.vacationWeekend)
        assertNull(facility.building)
        verify(placeClient).fetchBuildingDetail(2L)
    }

    @Test
    fun `place search maps buildings and campus places and forwards keyword`() = runTest {
        val building = building(id = 39L, name = "학생회관")
        whenever(placeClient.searchPlaces("학생회관")).thenReturn(
            success(
                PlaceSearchResponse(
                    buildings = listOf(building),
                    campusPlaces = listOf(
                        campusPlace(
                            id = 105L,
                            name = "학생회관 복사실",
                            category = "printer",
                            categoryKorName = "프린터",
                            floor = "1F",
                            building = building,
                        ),
                    ),
                ),
            ),
        )

        val result = repository.searchPlaces("학생회관").getOrThrow()

        assertEquals(1, result.buildings.size)
        assertEquals("39", result.buildings.single().id)
        assertEquals("학생회관", result.buildings.single().name)
        assertEquals("건물", result.buildings.single().category)
        assertEquals(Place.Priority.HIGH, result.buildings.single().priority)

        val facility = result.campusPlaces.single()
        assertEquals(105L, facility.id)
        assertEquals("학생회관 복사실", facility.name)
        assertEquals("printer", facility.category)
        assertEquals("프린터", facility.categoryKor)
        assertEquals("1F", facility.location)
        assertNull(facility.imageUrl)
        assertEquals(39L, facility.building?.id)
        verify(placeClient).searchPlaces("학생회관")
    }

    @Test
    fun `building list maps every building`() = runTest {
        whenever(placeClient.fetchBuildings()).thenReturn(
            success(
                PlaceBuildingListResponse(
                    buildings = listOf(
                        building(id = 1L, name = "행정관"),
                        building(id = 2L, name = "경영관"),
                    ),
                ),
            ),
        )

        val places = repository.getPlaceBuildings().getOrThrow()

        assertEquals(listOf("행정관", "경영관"), places.map(Place::name))
        assertTrue(places.all { it.category == "건물" })
        assertEquals(
            listOf(Place.Priority.MIDDLE, Place.Priority.HIGH),
            places.map(Place::priority),
        )
        verify(placeClient).fetchBuildings()
    }

    @Test
    fun `building list uses server display order for marker priority`() = runTest {
        whenever(placeClient.fetchBuildings()).thenReturn(
            success(
                PlaceBuildingListResponse(
                    buildings = listOf(
                        building(id = 1L, name = "행정관", displayOrder = 3),
                        building(id = 2L, name = "경영관", displayOrder = 2),
                    ),
                ),
            ),
        )

        val places = repository.getPlaceBuildings().getOrThrow()

        assertEquals(
            listOf(Place.Priority.LOW, Place.Priority.MIDDLE),
            places.map(Place::priority),
        )
    }

    @Test
    fun `campus places map category building location hours and optional metadata`() = runTest {
        val building = building(id = 2L, name = "경영관")
        val responses = listOf(
            campusPlace(
                id = 201L,
                name = "카페 레스티오",
                floor = "1F",
                locationDetail = "경영관 1층",
                building = building,
                imageUrl = "https://example.com/cafe.png",
                quantity = 2,
                externalUrl = "https://example.com/cafe",
                operatingHours = listOf(
                    operatingHours(
                        period = "SEMESTER",
                        dayGroup = "WEEKDAY",
                        status = "SCHEDULED",
                        opensAt = "08:00",
                        closesAt = "22:00",
                        isCurrent = true,
                    ),
                ),
            ),
            campusPlace(
                id = 202L,
                name = "경영관 휴게실",
                category = "lounge",
                categoryKorName = "휴게실",
                floor = "B1",
                locationDetail = "",
                building = building,
            ),
        )
        whenever(placeClient.fetchCampusPlaces(arrayOf("cafe", "lounge"))).thenReturn(
            success(PlaceCampusPlaceListResponse(campusPlaces = responses)),
        )

        val facilities = repository
            .getPlaceCampusPlaces(arrayOf("cafe", "lounge"))
            .getOrThrow()

        val cafe = facilities.first()
        assertEquals("경영관 1층", cafe.location)
        assertEquals("08:00 ~ 22:00", cafe.operationHours?.current)
        assertEquals("https://example.com/cafe.png", cafe.imageUrl)
        assertEquals(2, cafe.quantity)
        assertEquals("https://example.com/cafe", cafe.externalUrl)
        assertEquals(2L, cafe.building?.id)
        assertEquals("경영관", cafe.building?.name)
        assertEquals("B1", facilities.last().location)

        val categoriesCaptor = argumentCaptor<Array<String>>()
        verify(placeClient).fetchCampusPlaces(categoriesCaptor.capture())
        assertArrayEquals(arrayOf("cafe", "lounge"), categoriesCaptor.firstValue)
    }

    @Test
    fun `category list maps API names labels and display order`() = runTest {
        whenever(placeClient.fetchCategories()).thenReturn(
            success(
                PlaceCategoryListResponse(
                    categories = listOf(
                        PlaceCategoryResponse("cafe", "카페", 1),
                        PlaceCategoryResponse("restaurant", "식당", 2),
                    ),
                ),
            ),
        )

        val categories = repository.getPlaceCategories().getOrThrow()

        assertEquals("cafe", categories[0].name)
        assertEquals("카페", categories[0].korName)
        assertEquals(1, categories[0].displayOrder)
        assertEquals("restaurant", categories[1].name)
        verify(placeClient).fetchCategories()
    }

    @Test
    fun `unsuccessful API responses become failures for every repository call`() = runTest {
        whenever(placeClient.fetchBuildingDetail(2L)).thenReturn(failure("detail failed"))
        whenever(placeClient.searchPlaces("도서관")).thenReturn(failure("search failed"))
        whenever(placeClient.fetchBuildings()).thenReturn(failure("buildings failed"))
        whenever(placeClient.fetchCampusPlaces(arrayOf("cafe"))).thenReturn(
            failure("campus places failed"),
        )
        whenever(placeClient.fetchCategories()).thenReturn(failure("categories failed"))

        val failures = listOf(
            repository.getPlaceBuildingDetail(2L),
            repository.searchPlaces("도서관"),
            repository.getPlaceBuildings(),
            repository.getPlaceCampusPlaces(arrayOf("cafe")),
            repository.getPlaceCategories(),
        )

        assertTrue(failures.all(Result<*>::isFailure))
        assertEquals(
            listOf(
                "detail failed",
                "search failed",
                "buildings failed",
                "campus places failed",
                "categories failed",
            ),
            failures.map { it.exceptionOrNull()?.message },
        )
    }

    @Test
    fun `client exception is preserved as repository failure`() = runTest {
        val exception = IllegalStateException("offline")
        whenever(placeClient.fetchBuildings()).thenThrow(exception)

        val result = repository.getPlaceBuildings()

        assertTrue(result.isFailure)
        assertSame(exception, result.exceptionOrNull())
    }

    private fun building(
        id: Long,
        name: String,
        displayOrder: Int? = null,
    ) = PlaceBuildingResponse(
        id = id,
        name = name,
        address = "서울특별시 광진구 능동로 120",
        latitude = 37.5443474,
        longitude = 127.0761119,
        displayOrder = displayOrder,
    )

    private fun campusPlace(
        id: Long,
        name: String,
        category: String = "cafe",
        categoryKorName: String = "카페",
        floor: String? = null,
        locationDetail: String? = null,
        building: PlaceBuildingResponse? = null,
        imageUrl: String? = null,
        quantity: Int? = null,
        externalUrl: String? = null,
        operatingHours: List<PlaceOperatingHoursResponse> = emptyList(),
    ) = PlaceCampusPlaceResponse(
        id = id,
        name = name,
        category = category,
        categoryKorName = categoryKorName,
        imageUrl = imageUrl,
        locationType = if (floor == null) "OUTDOOR" else "INDOOR",
        floor = floor,
        locationDetail = locationDetail,
        quantity = quantity,
        operatingHours = operatingHours,
        externalUrl = externalUrl,
        building = building,
    )

    private fun operatingHours(
        period: String,
        dayGroup: String,
        status: String,
        opensAt: String? = null,
        closesAt: String? = null,
        isCurrent: Boolean = false,
    ) = PlaceOperatingHoursResponse(
        period = period,
        dayGroup = dayGroup,
        status = status,
        opensAt = opensAt,
        closesAt = closesAt,
        isCurrent = isCurrent,
    )

    private fun <T> success(data: T) = DefaultResponse(
        resultCode = 200,
        resultMsg = "success",
        data = data,
    )

    private fun <T> failure(message: String) = DefaultResponse<T>(
        resultCode = 500,
        resultMsg = message,
        data = null,
    )
}
