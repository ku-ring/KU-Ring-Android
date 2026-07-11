package com.ku_stacks.ku_ring.main.campusmap.type

import androidx.annotation.DrawableRes
import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.main.R

internal enum class CampusMapCategory(
    val label: String,
    private val matchingNames: Set<String>,
    @get:DrawableRes val iconRes: Int,
) {
    CAFE("교내 카페", setOf("카페", "교내 카페"), R.drawable.ic_campus_map_icon_cafe),
    RESTAURANT("식당", setOf("식당"), R.drawable.ic_campus_map_icon_restaurant),
    PRINT("프린터", setOf("프린터", "프린터기", "프린트", "복사", "복사실"), R.drawable.ic_campus_map_icon_print,
    ),
    SMOKE("흡연부스", setOf("흡연부스", "흡연"), R.drawable.ic_campus_map_icon_smoke),
    STORE("편의점", setOf("편의점", "매점"), R.drawable.ic_campus_map_icon_store),
    REST("휴게실", setOf("휴게실", "쉼터"), R.drawable.ic_campus_map_icon_rest),
    K_CUBE("k-cube", setOf("k-cube", "K-CUBE", "케이큐브"), R.drawable.ic_campus_map_icon_kcube);

    fun matches(category: String): Boolean =
        matchingNames.any { it.equals(category, ignoreCase = true) }

    companion object {
        @DrawableRes
        fun iconRes(category: String): Int = when {
            category.contains("카페") -> CAFE.iconRes
            category.contains("식당") -> RESTAURANT.iconRes
            category.contains("프린") || category.contains("복사") -> PRINT.iconRes
            category.contains("흡연") -> SMOKE.iconRes
            category.contains("편의점") || category.contains("매점") -> STORE.iconRes
            category.contains("휴게") || category.contains("쉼터") -> REST.iconRes
            category.contains("cube", ignoreCase = true) || category.contains("큐브") -> {
                K_CUBE.iconRes
            }

            else -> R.drawable.ic_campus_map_icon_building
        }
    }
}

internal fun filterCampusPlaces(
    places: List<Place>,
    selectedCategory: CampusMapCategory?,
): List<Place> {
    if (selectedCategory == null) return places

    return places.filter { place ->
        place.hasFacilityOf(selectedCategory)
    }
}

private fun Place.hasFacilityOf(category: CampusMapCategory): Boolean =
    facilities.any { facility -> category.matches(facility.category) }

internal fun searchCampusPlaces(
    places: List<Place>,
    query: String,
): List<Place> {
    val normalizedQuery = query.trim()
    if (normalizedQuery.isEmpty()) return emptyList()

    return places.filter { place ->
        place.containsSearchQuery(normalizedQuery)
    }
}

private fun Place.containsSearchQuery(query: String): Boolean =
    name.contains(query, ignoreCase = true) ||
            address.contains(query, ignoreCase = true) ||
            category.contains(query, ignoreCase = true) ||
            facilities.any { facility ->
                facility.name.contains(query, ignoreCase = true) ||
                        facility.category.contains(query, ignoreCase = true) ||
                        facility.location?.contains(query, ignoreCase = true) == true
            }
