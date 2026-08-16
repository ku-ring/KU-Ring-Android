package com.ku_stacks.ku_ring.main.campusmap

import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.PlaceFacility
import com.ku_stacks.ku_ring.main.campusmap.model.CampusMapRecentSearch
import com.ku_stacks.ku_ring.main.campusmap.model.CampusMapSearchResult
import com.ku_stacks.ku_ring.main.campusmap.model.buildCampusMapKeywordSearchResults
import com.ku_stacks.ku_ring.main.campusmap.model.buildCampusMapSearchResults
import com.ku_stacks.ku_ring.main.campusmap.model.mergeCampusMapSearchPlaces
import com.ku_stacks.ku_ring.main.campusmap.model.prioritizeRecentSearchResults
import com.ku_stacks.ku_ring.main.campusmap.type.CampusMapCategory
import com.ku_stacks.ku_ring.main.campusmap.type.CampusMapCategoryItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

internal data class CampusMapUiState(
    val focusedPlace: Place? = null,
    val selectedSearchPlace: Place? = null,
    val selectedSearchLabel: String? = null,
    val submittedSearchQuery: String? = null,
    val campusPlaces: ImmutableList<Place> = persistentListOf(),
    val categoryPlaces: ImmutableList<Place> = persistentListOf(),
    val searchPlaces: ImmutableList<Place> = persistentListOf(),
    val searchCampusPlaces: ImmutableList<PlaceFacility> = persistentListOf(),
    val searchResultQuery: String? = null,
    val liveSearchPlaces: ImmutableList<Place> = persistentListOf(),
    val liveSearchCampusPlaces: ImmutableList<PlaceFacility> = persistentListOf(),
    val liveSearchResultQuery: String? = null,
    val categories: ImmutableList<CampusMapCategoryItem> = persistentListOf(),
    val selectedCategories: ImmutableList<CampusMapCategory> = persistentListOf(),
    val searchInput: String = "",
    val recentSearches: ImmutableList<CampusMapRecentSearch> = persistentListOf(),
    val isInitialLoading: Boolean = false,
    val isCategoryLoading: Boolean = false,
    val isSubmittedSearchLoading: Boolean = false,
    val isLiveSearchLoading: Boolean = false,
    val pendingFocusedPlace: Place? = null,
    val isLocationPermissionDialogVisible: Boolean = false,
    val focusedPlaceSource: CampusMapFocusedPlaceSource? = null,
    val isInitialLoadFailed: Boolean = false,
    val failedCategories: ImmutableList<CampusMapCategory>? = null,
    val failedSubmittedSearchQuery: String? = null,
    val failedLiveSearchQuery: String? = null,
    val failedPlaceDetail: CampusMapFailedRequest.PlaceDetail? = null,
) {
    val mapFocusedPlace: Place?
        get() = focusedPlace ?: pendingFocusedPlace

    val visiblePlaces: ImmutableList<Place>
        get() {
            val hasCategory = selectedCategories.isNotEmpty()
            val searchedPlaces = if (hasCurrentSubmittedSearchResult) {
                mergeCampusMapSearchPlaces(
                    buildings = searchPlaces,
                    campusPlaces = searchCampusPlaces,
                    referenceBuildings = campusPlaces,
                )
            } else {
                emptyList()
            }

            return when {
                hasCategory && hasSubmittedQuery -> {
                    val categoryPlacesById = categoryPlaces.associateBy(Place::id)
                    searchedPlaces.mapNotNull { searchedPlace ->
                        val categoryPlace = categoryPlacesById[searchedPlace.id]
                            ?: return@mapNotNull null
                        searchedPlace.copy(
                            imageUrl = searchedPlace.imageUrl ?: categoryPlace.imageUrl,
                            facilities = (searchedPlace.facilities + categoryPlace.facilities)
                                .distinctBy(PlaceFacility::id),
                        )
                    }
                }

                hasCategory -> categoryPlaces
                hasSubmittedQuery -> searchedPlaces
                else -> campusPlaces
            }.toImmutableList()
        }

    val searchResults: ImmutableList<CampusMapSearchResult>
        get() {
            if (!hasSubmittedQuery) {
                return buildCampusMapSearchResults(
                    places = visiblePlaces,
                    selectedCategories = selectedCategories,
                ).toImmutableList()
            }
            if (!hasCurrentSubmittedSearchResult) return persistentListOf()

            val visiblePlaceIds = visiblePlaces.mapTo(mutableSetOf(), Place::id)
            val matchingBuildings = searchPlaces.filter { place -> place.id in visiblePlaceIds }
            val matchingCampusPlaces = searchCampusPlaces.filter { facility ->
                facility.building?.id?.toString() in visiblePlaceIds
            }
            return buildCampusMapKeywordSearchResults(
                buildings = matchingBuildings,
                campusPlaces = matchingCampusPlaces,
                referenceBuildings = campusPlaces,
            ).toImmutableList()
        }

    val liveSearchResults: ImmutableList<CampusMapSearchResult>
        get() {
            val normalizedInput = searchInput.trim()
            val matchingPlaces = liveSearchPlaces.takeIf {
                normalizedInput.isNotEmpty() && liveSearchResultQuery == normalizedInput
            }.orEmpty()
            val matchingCampusPlaces = liveSearchCampusPlaces.takeIf {
                normalizedInput.isNotEmpty() && liveSearchResultQuery == normalizedInput
            }.orEmpty()

            return prioritizeRecentSearchResults(
                results = buildCampusMapKeywordSearchResults(
                    buildings = matchingPlaces,
                    campusPlaces = matchingCampusPlaces,
                    referenceBuildings = campusPlaces,
                ),
                recentSearches = recentSearches,
            ).toImmutableList()
        }

    val activeSearchText: String?
        get() = selectedSearchLabel ?: selectedSearchPlace?.name ?: submittedSearchQuery

    val showSearchResultSheet: Boolean
        get() = mapFocusedPlace == null &&
            !isCategoryLoading &&
            !(hasSubmittedQuery && isSubmittedSearchLoading) &&
            !hasFailedActiveSelection &&
            (selectedCategories.isNotEmpty() || hasSubmittedQuery)

    val showCategoryChips: Boolean
        get() = mapFocusedPlace == null ||
            focusedPlaceSource == CampusMapFocusedPlaceSource.MAP_MARKER

    val shouldShowEmptySearchResult: Boolean
        get() = searchInput.isNotBlank() &&
            liveSearchResultQuery == searchInput.trim() &&
            !isLiveSearchLoading &&
            searchFailedRequest == null &&
            liveSearchResults.isEmpty()

    val isMapLoading: Boolean
        get() = isInitialLoading ||
            isCategoryLoading ||
            (hasSubmittedQuery && isSubmittedSearchLoading) ||
            pendingFocusedPlace != null

    val mapFailedRequest: CampusMapFailedRequest?
        get() = failedPlaceDetail
            ?: failedSubmittedSearchQuery
                ?.takeIf { query -> query == submittedSearchQuery }
                ?.let { query -> CampusMapFailedRequest.Search(query, isSubmitted = true) }
            ?: failedCategories
                ?.takeIf { categories -> categories == selectedCategories }
                ?.let { categories -> CampusMapFailedRequest.Category(categories) }
            ?: CampusMapFailedRequest.InitialData.takeIf { isInitialLoadFailed }

    val searchFailedRequest: CampusMapFailedRequest.Search?
        get() = failedLiveSearchQuery
            ?.takeIf { query -> query == searchInput.trim() }
            ?.let { query -> CampusMapFailedRequest.Search(query, isSubmitted = false) }

    private val hasSubmittedQuery: Boolean
        get() = !submittedSearchQuery.isNullOrBlank()

    private val hasCurrentSubmittedSearchResult: Boolean
        get() = hasSubmittedQuery && searchResultQuery == submittedSearchQuery

    private val hasFailedActiveSelection: Boolean
        get() = failedCategories?.let { categories -> categories == selectedCategories } == true ||
            failedSubmittedSearchQuery?.let { query -> query == submittedSearchQuery } == true

    companion object {
        val Empty = CampusMapUiState()
    }
}

internal enum class CampusMapFocusedPlaceSource {
    MAP_MARKER,
    SEARCH_RESULT,
}

internal sealed interface CampusMapFailedRequest {
    data object InitialData : CampusMapFailedRequest

    data class Category(
        val categories: ImmutableList<CampusMapCategory>,
    ) : CampusMapFailedRequest

    data class Search(
        val query: String,
        val isSubmitted: Boolean,
    ) : CampusMapFailedRequest

    data class PlaceDetail(
        val place: Place,
        val source: CampusMapFocusedPlaceSource,
    ) : CampusMapFailedRequest
}
