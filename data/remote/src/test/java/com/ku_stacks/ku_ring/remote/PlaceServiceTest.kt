package com.ku_stacks.ku_ring.remote

import com.ku_stacks.ku_ring.remote.place.PlaceService
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PlaceServiceTest : ApiAbstract<PlaceService>() {

    private lateinit var service: PlaceService

    @Before
    fun initService() {
        super.createMockServer()
        service = createService(PlaceService::class.java)
    }

    @After
    fun tearDown() {
        super.stopServer()
    }

    @Test
    fun `fetch buildings test`() = runTest {
        // given
        enqueueResponse("/PlaceBuildingListResponse.json")

        // when
        val response = service.fetchBuildings()
        mockWebServer.takeRequest()
        val buildings =
            response.data?.buildings ?: throw IllegalStateException("Data should not be null")

        // then
        assertEquals(200, response.resultCode)
        assertEquals(20, buildings.size)

        val first = buildings[0]
        assertEquals(1L, first.id)
        assertEquals("행정관", first.name)
        assertEquals("서울특별시 광진구 능동로 120", first.address)
        assertEquals(2, first.displayOrder)
    }

    @Test
    fun `fetch building detail test`() = runTest {
        // given
        enqueueResponse("/PlaceBuildingDetailResponse.json")

        // when
        val response = service.fetchBuildingDetail(2L)
        mockWebServer.takeRequest()
        val detail = response.data ?: throw IllegalStateException("Data should not be null")

        // then
        assertEquals(200, response.resultCode)
        assertEquals(2L, detail.id)
        assertEquals("경영관", detail.name)
        assertEquals(4, detail.operatingHours.size)
        assertEquals(5, detail.campusPlaces.size)

        // 실내 시설: floor가 채워져 있다
        val cafe = detail.campusPlaces.first { it.category == "cafe" }
        assertEquals("1F", cafe.floor)

        // 실외 시설(흡연부스): floor가 null로 내려온다
        val smokingBooth = detail.campusPlaces.first { it.category == "smoking_booth" }
        assertEquals("OUTDOOR", smokingBooth.locationType)
        assertNull(smokingBooth.floor)

        // 상세 조회의 campusPlaces에는 building이 내려오지 않는다
        assertTrue(detail.campusPlaces.all { it.building == null })
    }

    @Test
    fun `search places deserializes buildings and campus places`() = runTest {
        // given
        enqueueResponse("/PlaceBuildingSearchResponse.json")

        // when
        val response = service.searchPlaces("학생회관")
        val request = mockWebServer.takeRequest()
        val data = response.data ?: throw IllegalStateException("Data should not be null")
        val buildings = data.buildings
        val campusPlaces = data.campusPlaces

        // then
        assertEquals(200, response.resultCode)
        assertEquals(1, buildings.size)
        assertEquals("학생회관", buildings[0].name)
        assertNull(buildings[0].displayOrder)
        assertEquals(1, campusPlaces.size)
        assertEquals("학생회관 복사실", campusPlaces[0].name)
        assertEquals("printer", campusPlaces[0].category)
        assertEquals("프린터", campusPlaces[0].categoryKorName)
        assertNull(campusPlaces[0].imageUrl)
        assertEquals(39L, campusPlaces[0].building?.id)
        assertEquals("학생회관", campusPlaces[0].building?.name)
        assertEquals("학생회관", request.requestUrl?.queryParameter("keyword"))
    }

    @Test
    fun `fetch campus places by categories test`() = runTest {
        // given
        enqueueResponse("/PlaceCampusPlaceListResponse.json")

        // when
        val response = service.fetchCampusPlaces(arrayOf("cafe", "smoking_booth"))
        mockWebServer.takeRequest()
        val campusPlaces = response.data?.campusPlaces
            ?: throw IllegalStateException("Data should not be null")

        // then
        assertEquals(200, response.resultCode)
        assertEquals(9, campusPlaces.size)

        // campus-places 목록 조회에는 각 항목의 building이 채워져 있다
        assertTrue(campusPlaces.all { it.building != null })

        val smokingBooth = campusPlaces.first { it.category == "smoking_booth" }
        assertNull(smokingBooth.floor)
        assertEquals(2L, smokingBooth.building?.id)
    }

    @Test
    fun `fetch categories test`() = runTest {
        // given
        enqueueResponse("/PlaceCategoryListResponse.json")

        // when
        val response = service.fetchCategories()
        mockWebServer.takeRequest()
        val categories =
            response.data?.categories ?: throw IllegalStateException("Data should not be null")

        // then
        assertEquals(200, response.resultCode)
        assertEquals(7, categories.size)
        assertEquals("cafe", categories[0].name)
        assertEquals("카페", categories[0].korName)
    }
}
