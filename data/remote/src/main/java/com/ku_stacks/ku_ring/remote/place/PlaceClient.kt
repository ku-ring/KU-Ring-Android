package com.ku_stacks.ku_ring.remote.place

import com.ku_stacks.ku_ring.remote.place.response.PlaceBuildingDetailResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceBuildingListResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceCampusPlaceListResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceCategoryListResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceSearchResponse
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import javax.inject.Inject

class PlaceClient @Inject constructor(
    private val placeService: PlaceService,
) {
    suspend fun fetchBuildingDetail(buildingId: Long): DefaultResponse<PlaceBuildingDetailResponse> =
        placeService.fetchBuildingDetail(buildingId)

    suspend fun searchPlaces(keyword: String): DefaultResponse<PlaceSearchResponse> =
        placeService.searchPlaces(keyword)

    suspend fun fetchBuildings(): DefaultResponse<PlaceBuildingListResponse> =
        placeService.fetchBuildings()

    suspend fun fetchCampusPlaces(categories: Array<String>): DefaultResponse<PlaceCampusPlaceListResponse> =
        placeService.fetchCampusPlaces(categories)

    suspend fun fetchCategories(): DefaultResponse<PlaceCategoryListResponse> =
        placeService.fetchCategories()
}
