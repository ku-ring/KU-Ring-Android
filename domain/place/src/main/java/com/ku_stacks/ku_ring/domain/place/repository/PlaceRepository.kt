package com.ku_stacks.ku_ring.domain.place.repository

import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.PlaceCategory
import com.ku_stacks.ku_ring.domain.PlaceFacility

interface PlaceRepository {
    /**
     * 캠퍼스맵 건물의 상세 정보를 가져온다.
     *
     * @param buildingId 건물 ID
     * @return 정상 처리 시 건물 상세 정보를 [Result]에 담아 반환. 실패 시 [Result.Failure]
     */
    suspend fun getPlaceBuildingDetail(buildingId: Long): Result<Place>

    /**
     * 키워드로 캠퍼스맵 건물을 검색한다.
     *
     * @param keyword 검색 키워드
     * @return 정상 처리 시 검색된 건물 목록을 [Result]에 담아 반환. 실패 시 [Result.Failure]
     */
    suspend fun searchPlaceBuildings(keyword: String): Result<List<Place>>

    /**
     * 캠퍼스맵 전체 건물 목록을 가져온다.
     *
     * @return 정상 처리 시 건물 목록을 [Result]에 담아 반환. 실패 시 [Result.Failure]
     */
    suspend fun getPlaceBuildings(): Result<List<Place>>

    /**
     * 카테고리(들)에 해당하는 캠퍼스맵 시설 목록을 가져온다.
     *
     * @param categories 시설 카테고리 목록
     * @return 정상 처리 시 시설 목록을 [Result]에 담아 반환. 실패 시 [Result.Failure]
     */
    suspend fun getPlaceCampusPlaces(categories: Array<String>): Result<List<PlaceFacility>>

    /**
     * 캠퍼스맵 카테고리 목록을 가져온다.
     *
     * @return 정상 처리 시 카테고리 목록을 [Result]에 담아 반환. 실패 시 [Result.Failure]
     */
    suspend fun getPlaceCategories(): Result<List<PlaceCategory>>
}
