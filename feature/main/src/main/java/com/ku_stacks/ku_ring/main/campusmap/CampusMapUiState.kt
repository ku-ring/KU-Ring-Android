package com.ku_stacks.ku_ring.main.campusmap

import androidx.annotation.DrawableRes
import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.PlaceFacility
import com.ku_stacks.ku_ring.domain.PlaceOperationHours
import com.ku_stacks.ku_ring.main.R
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
) {
    val visiblePlaces: ImmutableList<Place>
        get() = if (hasSubmittedQuery) {
            searchCampusPlaces(
                places = campusPlaces,
                query = submittedSearchQuery.orEmpty(),
            )
        } else {
            filterCampusPlaces(
                places = campusPlaces,
                selectedCategory = selectedCategory,
            )
        }.toImmutableList()

    val searchResults: ImmutableList<CampusMapSearchResult>
        get() = buildCampusMapSearchResults(
            places = visiblePlaces,
            selectedCategory = selectedCategory,
        ).toImmutableList()

    val activeSearchText: String?
        get() = selectedCategory?.label ?: selectedSearchPlace?.name ?: submittedSearchQuery

    val showSearchResultSheet: Boolean
        get() = focusedPlace == null && (selectedCategory != null || hasSubmittedQuery)

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
        )
    }
}

internal data class CampusMapSearchResult(
    val id: String,
    val place: Place,
    val title: String,
    val category: String,
    val location: String,
    val operationHours: String,
    val imageUrl: String?,
)

internal sealed interface CampusMapRecentSearch {
    val id: String
    val label: String

    @get:DrawableRes
    val iconRes: Int

    data class Query(val query: String) : CampusMapRecentSearch {
        override val id: String = "query:${query.lowercase()}"
        override val label: String = query
        override val iconRes: Int = R.drawable.ic_campus_map_recent_search
    }

    data class PlaceResult(val place: Place) : CampusMapRecentSearch {
        override val id: String = "place:${place.id}"
        override val label: String = place.name
        override val iconRes: Int = CampusMapCategory.iconRes(place.category)
    }
}

internal fun buildCampusMapSearchResults(
    places: List<Place>,
    selectedCategory: CampusMapCategory?,
): List<CampusMapSearchResult> {
    if (selectedCategory == null) {
        return places.map(Place::toSearchResult)
    }

    return places.flatMap { place ->
        place.facilities.mapIndexedNotNull { index, facility ->
            if (selectedCategory.matches(facility.category)) {
                facility.toSearchResult(
                    place = place,
                    index = index,
                )
            } else {
                null
            }
        }
    }
}

internal fun updateRecentSearches(
    current: List<CampusMapRecentSearch>,
    selected: CampusMapRecentSearch,
    maxSize: Int = RECENT_SEARCH_MAX_SIZE,
): List<CampusMapRecentSearch> {
    return buildList {
        add(selected)
        current
            .filterNot { recentSearch -> recentSearch.id == selected.id }
            .take(maxSize - 1)
            .forEach(::add)
    }
}

private fun Place.toSearchResult() = CampusMapSearchResult(
    id = "place:$id",
    place = this,
    title = name,
    category = category,
    location = address,
    operationHours = operationHours.displayText()
        ?: facilities.firstNotNullOfOrNull { facility -> facility.operationHours.displayText() }
        ?: "-",
    imageUrl = imageUrl,
)

private fun PlaceFacility.toSearchResult(
    place: Place,
    index: Int,
) = CampusMapSearchResult(
    id = "facility:${place.id}:$index",
    place = place,
    title = name,
    category = category,
    location = location ?: place.address,
    operationHours = operationHours.displayText() ?: "-",
    imageUrl = place.imageUrl,
)

private fun PlaceOperationHours?.displayText(): String? =
    this?.current ?: this?.semester ?: this?.vacation

private const val RECENT_SEARCH_MAX_SIZE = 10
