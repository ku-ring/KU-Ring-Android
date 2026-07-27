package com.ku_stacks.ku_ring.main.campusmap

import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.main.campusmap.model.CampusMapRecentSearch
import com.ku_stacks.ku_ring.main.campusmap.model.CampusMapSearchResult
import com.ku_stacks.ku_ring.main.campusmap.model.buildCampusMapSearchResults
import com.ku_stacks.ku_ring.main.campusmap.model.prioritizeRecentSearchResults
import com.ku_stacks.ku_ring.main.campusmap.type.CampusMapCategory
import com.ku_stacks.ku_ring.main.campusmap.type.filterCampusPlaces
import com.ku_stacks.ku_ring.main.campusmap.type.searchCampusPlaces
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

internal data class CampusMapUiState(
    val focusedPlace: Place?,
    val selectedSearchPlace: Place?,
    val submittedSearchQuery: String?,
    val campusPlaces: ImmutableList<Place>,
    val selectedCategory: CampusMapCategory?,
    val searchInput: String,
    val recentSearches: ImmutableList<CampusMapRecentSearch>,
    val isLocationPermissionDialogVisible: Boolean,
    val focusedPlaceSource: CampusMapFocusedPlaceSource? = null,
) {
    val visiblePlaces: ImmutableList<Place>
        get() {
            val searchedPlaces = if (hasSubmittedQuery) {
                searchCampusPlaces(
                    places = campusPlaces,
                    query = submittedSearchQuery.orEmpty(),
                )
            } else {
                campusPlaces
            }

            return filterCampusPlaces(
                places = searchedPlaces,
                selectedCategory = selectedCategory,
            ).toImmutableList()
        }

    val searchResults: ImmutableList<CampusMapSearchResult>
        get() = buildCampusMapSearchResults(
            places = visiblePlaces,
            selectedCategory = selectedCategory.takeUnless { hasSubmittedQuery },
        ).toImmutableList()

    val liveSearchResults: ImmutableList<CampusMapSearchResult>
        get() = prioritizeRecentSearchResults(
            results = buildCampusMapSearchResults(
                places = searchCampusPlaces(
                    places = campusPlaces,
                    query = searchInput,
                ),
                selectedCategory = null,
            ),
            recentSearches = recentSearches,
        ).toImmutableList()

    val activeSearchText: String?
        get() = selectedSearchPlace?.name ?: submittedSearchQuery

    val showSearchResultSheet: Boolean
        get() = focusedPlace == null && (selectedCategory != null || hasSubmittedQuery)

    val showCategoryChips: Boolean
        get() = focusedPlace == null ||
            focusedPlaceSource == CampusMapFocusedPlaceSource.MAP_MARKER

    val shouldShowEmptySearchResult: Boolean
        get() = searchInput.isNotBlank() && liveSearchResults.isEmpty()

    private val hasSubmittedQuery: Boolean
        get() = !submittedSearchQuery.isNullOrBlank()

    companion object {
        val Empty = CampusMapUiState(
            focusedPlace = null,
            selectedSearchPlace = null,
            submittedSearchQuery = null,
            campusPlaces = persistentListOf(),
            selectedCategory = null,
            searchInput = "",
            recentSearches = persistentListOf(),
            isLocationPermissionDialogVisible = false,
        )
    }
}

internal enum class CampusMapFocusedPlaceSource {
    MAP_MARKER,
    SEARCH_RESULT,
}
