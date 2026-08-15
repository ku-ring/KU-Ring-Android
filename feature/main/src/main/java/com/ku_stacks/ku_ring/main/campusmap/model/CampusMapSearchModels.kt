package com.ku_stacks.ku_ring.main.campusmap.model

import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.PlaceFacility
import com.ku_stacks.ku_ring.domain.PlaceOperationHours
import com.ku_stacks.ku_ring.main.campusmap.type.CampusMapCategory

internal data class CampusMapSearchResult(
    val id: String,
    val place: Place,
    val title: String,
    val category: String,
    val location: String,
    val operationHours: String?,
    val imageUrl: String?,
    val isRecentMatch: Boolean = false,
)

internal sealed interface CampusMapRecentSearch {
    val id: String
    val label: String

    data class Query(val query: String) : CampusMapRecentSearch {
        override val id: String = "query:${query.lowercase()}"
        override val label: String = query
    }

    data class PlaceResult(val place: Place) : CampusMapRecentSearch {
        override val id: String = "place:${place.id}"
        override val label: String = place.name
    }
}

internal fun buildCampusMapSearchResults(
    places: List<Place>,
    selectedCategories: List<CampusMapCategory>,
): List<CampusMapSearchResult> {
    if (selectedCategories.isEmpty()) {
        return places.map(Place::toSearchResult)
    }

    return places.flatMap { place ->
        place.facilities.mapNotNull { facility ->
            if (selectedCategories.any { category -> category.matches(facility) }) {
                facility.toSearchResult(
                    place = place,
                )
            } else {
                null
            }
        }
    }
}

private fun CampusMapCategory.matches(facility: PlaceFacility): Boolean =
    matches(facility.category) || matches(facility.categoryKor)

internal fun prioritizeRecentSearchResults(
    results: List<CampusMapSearchResult>,
    recentSearches: List<CampusMapRecentSearch>,
): List<CampusMapSearchResult> {
    if (results.isEmpty() || recentSearches.isEmpty()) return results

    val resultsByTitle = results.groupBy { result ->
        result.title.toSearchComparisonKey()
    }
    val recentResults = recentSearches
        .flatMap { recentSearch ->
            resultsByTitle[recentSearch.label.toSearchComparisonKey()].orEmpty()
        }
        .distinctBy(CampusMapSearchResult::id)

    if (recentResults.isEmpty()) return results

    val recentResultIds = recentResults.mapTo(mutableSetOf(), CampusMapSearchResult::id)
    return buildList(results.size) {
        recentResults.forEach { result ->
            add(result.copy(isRecentMatch = true))
        }
        results
            .filterNot { result -> result.id in recentResultIds }
            .forEach(::add)
    }
}

internal fun updateRecentSearches(
    current: List<CampusMapRecentSearch>,
    selected: CampusMapRecentSearch,
    maxSize: Int = RECENT_SEARCH_MAX_SIZE,
): List<CampusMapRecentSearch> = buildList {
    add(selected)
    current
        .filterNot { recentSearch -> recentSearch.id == selected.id }
        .take(maxSize - 1)
        .forEach(::add)
}

private fun Place.toSearchResult() = CampusMapSearchResult(
    id = "place:$id",
    place = this,
    title = name,
    category = category,
    location = address,
    operationHours = operationHours.displayText(),
    imageUrl = imageUrl,
)

private fun PlaceFacility.toSearchResult(
    place: Place,
) = CampusMapSearchResult(
    id = "facility:$id",
    place = place,
    title = name,
    category = categoryKor.ifBlank { category },
    location = location ?: place.address,
    operationHours = operationHours.displayText(),
    imageUrl = imageUrl ?: place.imageUrl,
)

private fun PlaceOperationHours?.displayText(): String? =
    this?.current?.takeUnless(String::isBlank)

private fun String.toSearchComparisonKey(): String = trim().lowercase()

private const val RECENT_SEARCH_MAX_SIZE = 10
