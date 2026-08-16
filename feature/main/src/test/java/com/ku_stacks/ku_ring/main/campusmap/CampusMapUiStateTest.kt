package com.ku_stacks.ku_ring.main.campusmap

import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.PlaceBuilding
import com.ku_stacks.ku_ring.domain.PlaceFacility
import com.ku_stacks.ku_ring.main.campusmap.type.CampusMapCategory
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CampusMapUiStateTest {

    @Test
    fun `category chips remain visible for a place focused from a map marker`() {
        val state = CampusMapUiState(
            focusedPlace = place,
            focusedPlaceSource = CampusMapFocusedPlaceSource.MAP_MARKER,
        )

        assertTrue(state.showCategoryChips)
    }

    @Test
    fun `category chips are hidden for a place focused from a search result`() {
        val state = CampusMapUiState(
            focusedPlace = place,
            focusedPlaceSource = CampusMapFocusedPlaceSource.SEARCH_RESULT,
        )

        assertFalse(state.showCategoryChips)
    }

    @Test
    fun `a pending detail request focuses its summary marker and marks the map as loading`() {
        val state = CampusMapUiState(
            pendingFocusedPlace = place,
            focusedPlaceSource = CampusMapFocusedPlaceSource.MAP_MARKER,
        )

        assertEquals(place, state.mapFocusedPlace)
        assertTrue(state.showCategoryChips)
        assertTrue(state.isMapLoading)
        assertFalse(state.showSearchResultSheet)
    }

    @Test
    fun `base map uses buildings returned by the buildings API`() {
        val buildings = persistentListOf(
            place.copy(id = "1", name = "행정관"),
            place.copy(id = "2", name = "경영관"),
        )

        val state = CampusMapUiState(campusPlaces = buildings)

        assertEquals(buildings, state.visiblePlaces)
        assertFalse(state.showSearchResultSheet)
    }

    @Test
    fun `category-only selection uses grouped category places and creates facility rows`() {
        val categoryPlace = place.copy(
            id = "2",
            name = "경영관",
            facilities = listOf(
                facility(
                    id = 201L,
                    name = "카페 레스티오",
                    category = "cafe",
                    categoryKor = "카페",
                ),
            ),
        )
        val state = CampusMapUiState(
            categoryPlaces = persistentListOf(categoryPlace),
            selectedCategories = persistentListOf(CampusMapCategory.CAFE),
        )

        assertEquals(listOf(categoryPlace), state.visiblePlaces)
        assertEquals(listOf("카페 레스티오"), state.searchResults.map { it.title })
        assertTrue(state.showSearchResultSheet)
    }

    @Test
    fun `a category intersects submitted API search results without converting them to facility rows`() {
        val restaurantSearchPlace = place.copy(id = "4", name = "201 식당 건물")
        val cafeSearchPlace = place.copy(id = "5", name = "201 카페 건물")
        val restaurantCategoryPlace = restaurantSearchPlace.copy(
            facilities = listOf(
                facility(
                    id = 401L,
                    name = "학생식당",
                    category = "restaurant",
                    categoryKor = "식당",
                ),
            ),
        )
        val state = CampusMapUiState(
            submittedSearchQuery = "201",
            categoryPlaces = persistentListOf(restaurantCategoryPlace),
            searchPlaces = persistentListOf(restaurantSearchPlace, cafeSearchPlace),
            searchResultQuery = "201",
            selectedCategories = persistentListOf(CampusMapCategory.RESTAURANT),
            searchInput = "201",
        )

        assertEquals(listOf("4"), state.visiblePlaces.map { it.id })
        assertEquals(listOf("학생식당"), state.visiblePlaces.single().facilities.map { it.name })
        assertEquals(listOf("201 식당 건물"), state.searchResults.map { it.title })
    }

    @Test
    fun `a category intersects unified facility results by parent building`() {
        val managementBuilding = building(id = 2L, name = "경영관")
        val cafeSearchResult = facility(
            id = 201L,
            name = "카페 레스티오",
            category = "cafe",
            categoryKor = "카페",
            building = managementBuilding,
        )
        val printerCategoryPlace = place.copy(
            id = "2",
            name = "경영관",
            facilities = listOf(
                facility(
                    id = 202L,
                    name = "경영관 복사실",
                    category = "printer",
                    categoryKor = "프린터",
                ),
            ),
        )
        val state = CampusMapUiState(
            submittedSearchQuery = "카페",
            categoryPlaces = persistentListOf(printerCategoryPlace),
            searchCampusPlaces = persistentListOf(cafeSearchResult),
            searchResultQuery = "카페",
            selectedCategories = persistentListOf(CampusMapCategory.PRINT),
            searchInput = "카페",
        )

        assertEquals(listOf("2"), state.visiblePlaces.map { it.id })
        assertEquals(
            listOf("카페 레스티오", "경영관 복사실"),
            state.visiblePlaces.single().facilities.map { it.name },
        )
        assertEquals(listOf("카페 레스티오"), state.searchResults.map { it.title })
    }

    @Test
    fun `multiple categories keep their facilities on an intersected search marker`() {
        val searchPlace = place.copy(id = "2", name = "경영관")
        val cafe = facility(
            id = 201L,
            name = "카페 레스티오",
            category = "cafe",
            categoryKor = "카페",
        )
        val printer = facility(
            id = 202L,
            name = "경영관 복사실",
            category = "printer",
            categoryKor = "프린터",
        )
        val state = CampusMapUiState(
            submittedSearchQuery = "경영관",
            categoryPlaces = persistentListOf(
                searchPlace.copy(facilities = listOf(cafe, printer)),
            ),
            searchPlaces = persistentListOf(searchPlace),
            searchResultQuery = "경영관",
            selectedCategories = persistentListOf(
                CampusMapCategory.CAFE,
                CampusMapCategory.PRINT,
            ),
            searchInput = "경영관",
        )

        assertEquals(listOf(cafe, printer), state.visiblePlaces.single().facilities)
        assertEquals(listOf("경영관"), state.searchResults.map { it.title })
    }

    @Test
    fun `stale search responses are not shown for a newer submitted query`() {
        val state = CampusMapUiState(
            submittedSearchQuery = "도서관",
            searchPlaces = persistentListOf(place.copy(id = "2", name = "경영관")),
            searchResultQuery = "경영관",
        )

        assertTrue(state.visiblePlaces.isEmpty())
        assertTrue(state.searchResults.isEmpty())
    }

    @Test
    fun `live search uses only the response matching the normalized input`() {
        val searchPlace = place.copy(id = "10", name = "상허기념도서관")
        val matchingState = CampusMapUiState(
            searchInput = "  도서관  ",
            liveSearchPlaces = persistentListOf(searchPlace),
            liveSearchResultQuery = "도서관",
        )
        val staleState = matchingState.copy(liveSearchResultQuery = "경영관")

        assertEquals(listOf("상허기념도서관"), matchingState.liveSearchResults.map { it.title })
        assertTrue(staleState.liveSearchResults.isEmpty())
    }

    @Test
    fun `live draft results never replace committed map search results`() {
        val library = place.copy(id = "10", name = "상허기념도서관")
        val management = place.copy(id = "2", name = "경영관")
        val state = CampusMapUiState(
            submittedSearchQuery = "도서관",
            searchPlaces = persistentListOf(library),
            searchResultQuery = "도서관",
            searchInput = "경영관",
            liveSearchPlaces = persistentListOf(management),
            liveSearchResultQuery = "경영관",
        )

        assertEquals(listOf(library), state.visiblePlaces)
        assertEquals(listOf("상허기념도서관"), state.searchResults.map { it.title })
        assertEquals(listOf("경영관"), state.liveSearchResults.map { it.title })
    }

    @Test
    fun `facility-only submitted search keeps facility rows and one parent marker`() {
        val building = building(id = 2L, name = "경영관")
        val canonicalBuilding = place.copy(
            id = "2",
            name = "경영관",
            priority = Place.Priority.LOW,
        )
        val cafe = facility(
            id = 201L,
            name = "카페 레스티오",
            category = "cafe",
            categoryKor = "카페",
            building = building,
        )
        val printer = facility(
            id = 202L,
            name = "경영관 복사실",
            category = "printer",
            categoryKor = "프린터",
            building = building,
        )
        val state = CampusMapUiState(
            submittedSearchQuery = "경영관",
            campusPlaces = persistentListOf(canonicalBuilding),
            searchCampusPlaces = persistentListOf(cafe, printer),
            searchResultQuery = "경영관",
            searchInput = "경영관",
        )

        assertEquals(listOf("2"), state.visiblePlaces.map { it.id })
        assertEquals(Place.Priority.LOW, state.visiblePlaces.single().priority)
        assertEquals(listOf("카페 레스티오", "경영관 복사실"), state.searchResults.map { it.title })
        assertTrue(state.showSearchResultSheet)
    }

    @Test
    fun `facility-only live search is not treated as empty`() {
        val cafe = facility(
            id = 201L,
            name = "카페 레스티오",
            category = "cafe",
            categoryKor = "카페",
            building = building(id = 2L, name = "경영관"),
        )
        val state = CampusMapUiState(
            searchInput = "카페",
            liveSearchCampusPlaces = persistentListOf(cafe),
            liveSearchResultQuery = "카페",
        )

        assertEquals(listOf("카페 레스티오"), state.liveSearchResults.map { it.title })
        assertFalse(state.shouldShowEmptySearchResult)
    }

    @Test
    fun `map and live search failures expose only errors for their current contexts`() {
        val currentFailures = CampusMapUiState(
            submittedSearchQuery = "도서관",
            searchInput = "경영관",
            failedSubmittedSearchQuery = "도서관",
            failedLiveSearchQuery = "경영관",
        )
        val staleFailures = currentFailures.copy(
            failedSubmittedSearchQuery = "경영관",
            failedLiveSearchQuery = "도서관",
        )

        assertEquals(
            CampusMapFailedRequest.Search(query = "도서관", isSubmitted = true),
            currentFailures.mapFailedRequest,
        )
        assertEquals(
            CampusMapFailedRequest.Search(query = "경영관", isSubmitted = false),
            currentFailures.searchFailedRequest,
        )
        assertNull(staleFailures.mapFailedRequest)
        assertNull(staleFailures.searchFailedRequest)
    }

    @Test
    fun `success in one search channel does not hide an error in the other channel`() {
        val library = place.copy(id = "10", name = "상허기념도서관")
        val management = place.copy(id = "2", name = "경영관")
        val liveSuccessWithSubmittedFailure = CampusMapUiState(
            submittedSearchQuery = "도서관",
            failedSubmittedSearchQuery = "도서관",
            searchInput = "경영관",
            liveSearchPlaces = persistentListOf(management),
            liveSearchResultQuery = "경영관",
        )
        val submittedSuccessWithLiveFailure = CampusMapUiState(
            submittedSearchQuery = "도서관",
            searchPlaces = persistentListOf(library),
            searchResultQuery = "도서관",
            searchInput = "경영관",
            failedLiveSearchQuery = "경영관",
        )

        assertEquals(
            CampusMapFailedRequest.Search(query = "도서관", isSubmitted = true),
            liveSuccessWithSubmittedFailure.mapFailedRequest,
        )
        assertNull(liveSuccessWithSubmittedFailure.searchFailedRequest)
        assertEquals(listOf("경영관"), liveSuccessWithSubmittedFailure.liveSearchResults.map { it.title })

        assertNull(submittedSuccessWithLiveFailure.mapFailedRequest)
        assertEquals(
            CampusMapFailedRequest.Search(query = "경영관", isSubmitted = false),
            submittedSuccessWithLiveFailure.searchFailedRequest,
        )
        assertEquals(listOf(library), submittedSuccessWithLiveFailure.visiblePlaces)
    }

    @Test
    fun `empty live search state waits for a successful API response`() {
        val beforeResponse = CampusMapUiState(searchInput = "없는 건물")
        val noResult = beforeResponse.copy(liveSearchResultQuery = "없는 건물")
        val loading = beforeResponse.copy(isLiveSearchLoading = true)
        val failed = noResult.copy(
            failedLiveSearchQuery = "없는 건물",
        )
        val withResult = noResult.copy(
            liveSearchPlaces = persistentListOf(place.copy(name = "검색되는 건물")),
            liveSearchResultQuery = "없는 건물",
        )

        assertFalse(beforeResponse.shouldShowEmptySearchResult)
        assertTrue(noResult.shouldShowEmptySearchResult)
        assertFalse(loading.shouldShowEmptySearchResult)
        assertFalse(failed.shouldShowEmptySearchResult)
        assertFalse(withResult.shouldShowEmptySearchResult)
    }

    @Test
    fun `active category loading or failure suppresses the result sheet`() {
        val selectedCategories = persistentListOf(CampusMapCategory.CAFE)
        val selected = CampusMapUiState(selectedCategories = selectedCategories)
        val loading = selected.copy(isCategoryLoading = true)
        val failed = selected.copy(
            failedCategories = selectedCategories,
        )
        val unrelatedFailure = selected.copy(
            failedCategories = persistentListOf(CampusMapCategory.PRINT),
        )

        assertTrue(selected.showSearchResultSheet)
        assertFalse(loading.showSearchResultSheet)
        assertFalse(failed.showSearchResultSheet)
        assertTrue(unrelatedFailure.showSearchResultSheet)
    }

    @Test
    fun `empty state has no focused place and is not loading`() {
        assertNull(CampusMapUiState.Empty.mapFocusedPlace)
        assertFalse(CampusMapUiState.Empty.isMapLoading)
        assertTrue(CampusMapUiState.Empty.showCategoryChips)
    }

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

        fun facility(
            id: Long,
            name: String,
            category: String,
            categoryKor: String,
            building: PlaceBuilding? = null,
        ) = PlaceFacility(
            id = id,
            name = name,
            category = category,
            categoryKor = categoryKor,
            building = building,
        )

        fun building(
            id: Long,
            name: String,
        ) = PlaceBuilding(
            id = id,
            name = name,
            address = "서울특별시 광진구 능동로 120",
            latitude = 37.542366,
            longitude = 127.076846,
        )
    }
}
