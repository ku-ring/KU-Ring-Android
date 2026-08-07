package com.ku_stacks.ku_ring.place.repository

import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.PlaceCategory
import com.ku_stacks.ku_ring.domain.PlaceFacility
import com.ku_stacks.ku_ring.domain.place.repository.PlaceRepository
import com.ku_stacks.ku_ring.place.mapper.toDomain
import com.ku_stacks.ku_ring.remote.place.PlaceClient
import com.ku_stacks.ku_ring.util.suspendRunCatching
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val placeClient: PlaceClient,
) : PlaceRepository {
    override suspend fun getPlaceBuildingDetail(buildingId: Long): Result<Place> =
        suspendRunCatching {
            val response = placeClient.fetchBuildingDetail(buildingId)
            when {
                response.isSuccessAndDataExists -> response.data!!.toDomain()
                else -> throw IllegalStateException(response.resultMsg)
            }
        }

    override suspend fun searchPlaceBuildings(keyword: String): Result<List<Place>> =
        suspendRunCatching {
            val response = placeClient.searchBuildings(keyword)
            when {
                response.isSuccessAndDataExists -> response.data!!.buildings.map { it.toDomain() }
                else -> throw IllegalStateException(response.resultMsg)
            }
        }

    override suspend fun getPlaceBuildings(): Result<List<Place>> = suspendRunCatching {
        val response = placeClient.fetchBuildings()
        when {
            response.isSuccessAndDataExists -> response.data!!.buildings.map { it.toDomain() }
            else -> throw IllegalStateException(response.resultMsg)
        }
    }

    override suspend fun getPlaceCampusPlaces(categories: Array<String>): Result<List<PlaceFacility>> =
        suspendRunCatching {
            val response = placeClient.fetchCampusPlaces(categories)
            when {
                response.isSuccessAndDataExists -> response.data!!.campusPlaces.map { it.toDomain() }
                else -> throw IllegalStateException(response.resultMsg)
            }
        }

    override suspend fun getPlaceCategories(): Result<List<PlaceCategory>> = suspendRunCatching {
        val response = placeClient.fetchCategories()
        when {
            response.isSuccessAndDataExists -> response.data!!.categories.map { it.toDomain() }
            else -> throw IllegalStateException(response.resultMsg)
        }
    }
}
