package com.ku_stacks.ku_ring.domain.place.repository

import com.ku_stacks.ku_ring.domain.Place

interface PlaceRepository {
    suspend fun getPlaces(): List<Place>
}