package com.ku_stacks.ku_ring.main.campusmap

import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.PlaceFacility
import com.ku_stacks.ku_ring.main.campusmap.type.CampusMapCategory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CampusMapUiStateTest {

    @Test
    fun `category chips remain visible for a place focused from a map marker`() {
        val state = uiState(
            focusedPlace = place,
            focusedPlaceSource = CampusMapFocusedPlaceSource.MAP_MARKER,
        )

        assertTrue(state.showCategoryChips)
    }

    @Test
    fun `category chips are hidden for a place focused from a search result`() {
        val state = uiState(
            focusedPlace = place,
            focusedPlaceSource = CampusMapFocusedPlaceSource.SEARCH_RESULT,
        )

        assertFalse(state.showCategoryChips)
    }

    @Test
    fun `category chips are visible when no place is focused`() {
        val state = uiState()

        assertTrue(state.showCategoryChips)
    }

    @Test
    fun `a category filters submitted search results without converting them to facility rows`() {
        val restaurantPlace = place.copy(
            id = "restaurant-place",
            name = "201 식당 건물",
            facilities = listOf(
                PlaceFacility(
                    id = 1,
                    name = "학생식당",
                    categoryKor = "식당",
                    category = "식당",
                ),
            ),
        )
        val cafePlace = place.copy(
            id = "cafe-place",
            name = "201 카페 건물",
            facilities = listOf(
                PlaceFacility(
                    id = 2,
                    name = "학생 카페",
                    categoryKor = "카페",
                    category = "카페",
                ),
            ),
        )
        val state = uiState(
            submittedSearchQuery = "201",
            campusPlaces = persistentListOf(restaurantPlace, cafePlace),
            selectedCategory = CampusMapCategory.RESTAURANT,
            searchInput = "201",
        )

        assertEquals(listOf(restaurantPlace), state.visiblePlaces)
        assertEquals(listOf("201 식당 건물"), state.searchResults.map { it.title })
    }

    @Test
    fun `empty result state is shown immediately for a non-blank query without results`() {
        val emptyQuery = uiState()
        val queryWithoutResults = uiState(searchInput = "없는 건물")
        val queryWithResults = uiState(
            campusPlaces = persistentListOf(
                place.copy(name = "검색되는 건물"),
            ),
            searchInput = "검색되는",
        )

        assertFalse(emptyQuery.shouldShowEmptySearchResult)
        assertTrue(queryWithoutResults.shouldShowEmptySearchResult)
        assertFalse(queryWithResults.shouldShowEmptySearchResult)
    }

    private fun uiState(
        focusedPlace: Place? = null,
        submittedSearchQuery: String? = null,
        campusPlaces: ImmutableList<Place> = persistentListOf(),
        selectedCategory: CampusMapCategory? = null,
        searchInput: String = "",
        focusedPlaceSource: CampusMapFocusedPlaceSource? = null,
    ) = CampusMapUiState(
        focusedPlace = focusedPlace,
        selectedSearchPlace = null,
        submittedSearchQuery = submittedSearchQuery,
        campusPlaces = campusPlaces,
        selectedCategory = selectedCategory,
        searchInput = searchInput,
        recentSearches = persistentListOf(),
        isLocationPermissionDialogVisible = false,
        focusedPlaceSource = focusedPlaceSource,
    )

    private companion object {
        val place = Place(
            id = "place-id",
            name = "건물",
            category = "건물",
            address = "서울특별시 광진구 능동로 120",
            latitude = 37.542366,
            longitude = 127.076846,
            priority = Place.Priority.HIGH,
        )
    }
}
