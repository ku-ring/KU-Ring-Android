package com.ku_stacks.ku_ring.remote.place

import com.ku_stacks.ku_ring.remote.place.response.PlaceBuildingDetailResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceBuildingListResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceCampusPlaceListResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceCategoryListResponse
import com.ku_stacks.ku_ring.remote.place.response.PlaceSearchResponse
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaceService {
    @GET("v2/maps/buildings/{buildingId}")
    suspend fun fetchBuildingDetail(
        @Path("buildingId") buildingId: Long,
    ): DefaultResponse<PlaceBuildingDetailResponse>

    @GET("v2/maps/buildings/search")
    suspend fun searchPlaces(
        @Query("keyword") keyword: String,
    ): DefaultResponse<PlaceSearchResponse>

    @GET("v2/maps/buildings")
    suspend fun fetchBuildings(): DefaultResponse<PlaceBuildingListResponse>

    @GET("v2/maps/campus-places")
    suspend fun fetchCampusPlaces(
        @Query("categories") categories: Array<String>,
    ): DefaultResponse<PlaceCampusPlaceListResponse>

    @GET("v2/maps/categories")
    suspend fun fetchCategories(): DefaultResponse<PlaceCategoryListResponse>
}
