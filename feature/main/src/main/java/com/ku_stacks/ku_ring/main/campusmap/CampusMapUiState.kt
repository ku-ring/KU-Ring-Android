package com.ku_stacks.ku_ring.main.campusmap

import com.ku_stacks.ku_ring.domain.Place
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class CampusMapUiState (
    val focusedPlace: Place?,
    val campusPlaces: ImmutableList<Place>,
) {
    companion object {
        val Empty = CampusMapUiState(
            focusedPlace = null,
            campusPlaces = emptyList<Place>().toImmutableList(),
        )
    }
}