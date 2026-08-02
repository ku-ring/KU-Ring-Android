package com.ku_stacks.ku_ring.main.campusmap.model

import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.PlaceFacility
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
            selectedCategory = null,
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
    fun `category search results contain only matching facilities`() {
        val place = place(
            id = "facilities",
            name = "복합 건물",
            facilities = listOf(
                PlaceFacility(
                    id = 1,
                    name = "학생식당",
                    category = "식당",
                    categoryKor = "식당",
                ),
                PlaceFacility(
                    id = 2,
                    name = "복사실",
                    category = "프린터",
                    categoryKor = "프린터",
                ),
            ),
        )

        val results = buildCampusMapSearchResults(
            places = listOf(place),
            selectedCategory = CampusMapCategory.RESTAURANT,
        )

        assertEquals(1, results.size)
        assertEquals("학생식당", results.single().title)
        assertEquals("식당", results.single().category)
    }

    private fun place(
        id: String,
        name: String,
        facilities: List<PlaceFacility> = emptyList(),
    ) = Place(
        id = id,
        name = name,
        category = "건물",
        address = "서울특별시 광진구 능동로 120",
        latitude = 37.542366,
        longitude = 127.076846,
        priority = Place.Priority.HIGH,
        facilities = facilities,
    )
}
