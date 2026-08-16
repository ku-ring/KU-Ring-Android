package com.ku_stacks.ku_ring.domain

data class PlaceSearchResult(
    val buildings: List<Place>,
    val campusPlaces: List<PlaceFacility>,
)
