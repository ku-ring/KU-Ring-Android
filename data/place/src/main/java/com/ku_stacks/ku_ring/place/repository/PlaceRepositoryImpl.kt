package com.ku_stacks.ku_ring.place.repository

import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.place.repository.PlaceRepository
import com.ku_stacks.ku_ring.place.datasource.PlaceDataSource
import com.ku_stacks.ku_ring.place.mapper.toDomain
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val placeDataSource: PlaceDataSource,
): PlaceRepository {
    override suspend fun getPlaces(): List<Place> {
        return placeDataSource.getJsonPlaces().map { it.toDomain() }
    }
}
