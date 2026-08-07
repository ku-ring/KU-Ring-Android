package com.ku_stacks.ku_ring.main.campusmap.model

import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.PlaceFacility
import com.ku_stacks.ku_ring.domain.PlaceOperationHours
import com.ku_stacks.ku_ring.main.campusmap.type.CampusMapCategory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CampusMapSearchModelsTest {

    @Test
    fun `recent search matches are moved to the top and marked as recent`() {
        val firstPlace = place(id = "first", name = "첫 번째 건물")
        val recentPlace = place(id = "recent", name = "최근 건물")
        val results = buildCampusMapSearchResults(
            places = listOf(firstPlace, recentPlace),
            selectedCategories = emptyList(),
        )

        val prioritizedResults = prioritizeRecentSearchResults(
            results = results,
            recentSearches = listOf(CampusMapRecentSearch.PlaceResult(recentPlace)),
        )

        assertEquals(listOf("최근 건물", "첫 번째 건물"), prioritizedResults.map { it.title })
        assertTrue(prioritizedResults.first().isRecentMatch)
        assertFalse(prioritizedResults.last().isRecentMatch)
    }

    @Test
    fun `selecting an existing recent search moves it to the front without duplication`() {
        val first = CampusMapRecentSearch.Query("첫 번째")
        val selected = CampusMapRecentSearch.Query("선택")
        val third = CampusMapRecentSearch.Query("세 번째")

        val updated = updateRecentSearches(
            current = listOf(first, selected, third),
            selected = selected,
            maxSize = 3,
        )

        assertEquals(listOf(selected, first, third), updated)
    }

    @Test
    fun `category results match API category code and display Korean category`() {
        val place = place(
            id = "facilities",
            name = "복합 건물",
            facilities = listOf(
                facility(
                    id = 201L,
                    name = "학생식당",
                    category = "restaurant",
                    categoryKor = "식당",
                ),
                facility(
                    id = 202L,
                    name = "복사실",
                    category = "printer",
                    categoryKor = "프린터",
                ),
            ),
        )

        val results = buildCampusMapSearchResults(
            places = listOf(place),
            selectedCategories = listOf(CampusMapCategory.RESTAURANT),
        )

        assertEquals(1, results.size)
        assertEquals("facility:201", results.single().id)
        assertEquals("학생식당", results.single().title)
        assertEquals("식당", results.single().category)
    }

    @Test
    fun `multiple categories create facility results for every selected category`() {
        val place = place(
            id = "facilities",
            name = "복합 건물",
            facilities = listOf(
                facility(
                    id = 201L,
                    name = "학생식당",
                    category = "restaurant",
                    categoryKor = "식당",
                ),
                facility(
                    id = 202L,
                    name = "복사실",
                    category = "printer",
                    categoryKor = "프린터",
                ),
                facility(
                    id = 203L,
                    name = "교내 카페",
                    category = "cafe",
                    categoryKor = "카페",
                ),
            ),
        )

        val results = buildCampusMapSearchResults(
            places = listOf(place),
            selectedCategories = listOf(
                CampusMapCategory.RESTAURANT,
                CampusMapCategory.PRINT,
            ),
        )

        assertEquals(listOf("학생식당", "복사실"), results.map { it.title })
    }

    @Test
    fun `category results also match Korean category aliases`() {
        val place = place(
            id = "legacy-category",
            name = "복합 건물",
            facilities = listOf(
                facility(
                    id = 301L,
                    name = "무인 프린터기",
                    category = "프린터기",
                    categoryKor = "",
                ),
            ),
        )

        val results = buildCampusMapSearchResults(
            places = listOf(place),
            selectedCategories = listOf(CampusMapCategory.PRINT),
        )

        assertEquals(listOf("무인 프린터기"), results.map { it.title })
        assertEquals("프린터기", results.single().category)
    }

    @Test
    fun `facility result prefers facility image and current operating hours`() {
        val result = buildCampusMapSearchResults(
            places = listOf(
                place(
                    id = "2",
                    name = "경영관",
                    imageUrl = "https://example.com/building.png",
                    facilities = listOf(
                        facility(
                            id = 201L,
                            name = "카페 레스티오",
                            category = "cafe",
                            categoryKor = "카페",
                            imageUrl = "https://example.com/cafe.png",
                            operationHours = PlaceOperationHours(
                                current = "09:00 ~ 18:00",
                                semesterWeekday = "08:00 ~ 22:00",
                            ),
                        ),
                    ),
                ),
            ),
            selectedCategories = listOf(CampusMapCategory.CAFE),
        ).single()

        assertEquals("https://example.com/cafe.png", result.imageUrl)
        assertEquals("09:00 ~ 18:00", result.operationHours)
    }

    @Test
    fun `facility result uses the first available structured period hours`() {
        val result = buildCampusMapSearchResults(
            places = listOf(
                place(
                    id = "2",
                    name = "경영관",
                    facilities = listOf(
                        facility(
                            id = 201L,
                            name = "카페 레스티오",
                            category = "cafe",
                            categoryKor = "카페",
                            operationHours = PlaceOperationHours(
                                semesterWeekend = "09:00 ~ 18:00",
                            ),
                        ),
                    ),
                ),
            ),
            selectedCategories = listOf(CampusMapCategory.CAFE),
        ).single()

        assertEquals("09:00 ~ 18:00", result.operationHours)
    }

    @Test
    fun `facility result shows dash when every structured period is unavailable`() {
        val result = buildCampusMapSearchResults(
            places = listOf(
                place(
                    id = "2",
                    name = "경영관",
                    facilities = listOf(
                        facility(
                            id = 201L,
                            name = "카페 레스티오",
                            category = "cafe",
                            categoryKor = "카페",
                            operationHours = PlaceOperationHours(),
                        ),
                    ),
                ),
            ),
            selectedCategories = listOf(CampusMapCategory.CAFE),
        ).single()

        assertEquals("-", result.operationHours)
    }

    @Test
    fun `facility result falls back to building address image and dash`() {
        val building = place(
            id = "4",
            name = "학생회관",
            imageUrl = "https://example.com/building.png",
            facilities = listOf(
                facility(
                    id = 401L,
                    name = "학생식당",
                    category = "restaurant",
                    categoryKor = "식당",
                ),
            ),
        )

        val result = buildCampusMapSearchResults(
            places = listOf(building),
            selectedCategories = listOf(CampusMapCategory.RESTAURANT),
        ).single()

        assertEquals(building.address, result.location)
        assertEquals(building.imageUrl, result.imageUrl)
        assertEquals("-", result.operationHours)
    }

    private fun place(
        id: String,
        name: String,
        imageUrl: String? = null,
        facilities: List<PlaceFacility> = emptyList(),
    ) = Place(
        id = id,
        name = name,
        category = "건물",
        address = "서울특별시 광진구 능동로 120",
        latitude = 37.542366,
        longitude = 127.076846,
        priority = Place.Priority.HIGH,
        imageUrl = imageUrl,
        facilities = facilities,
    )

    private fun facility(
        id: Long,
        name: String,
        category: String,
        categoryKor: String,
        imageUrl: String? = null,
        operationHours: PlaceOperationHours? = null,
    ) = PlaceFacility(
        id = id,
        name = name,
        category = category,
        categoryKor = categoryKor,
        imageUrl = imageUrl,
        operationHours = operationHours,
    )
}
