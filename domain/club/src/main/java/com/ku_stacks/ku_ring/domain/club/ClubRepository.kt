package com.ku_stacks.ku_ring.domain.club

import androidx.paging.PagingData
import com.ku_stacks.ku_ring.domain.Club
import com.ku_stacks.ku_ring.domain.ClubCategory
import com.ku_stacks.ku_ring.domain.ClubDivision
import com.ku_stacks.ku_ring.domain.ClubSummary
import kotlinx.coroutines.flow.Flow

interface ClubRepository {
    /**
     * 동아리를 구독한다.
     *
     * @param clubId 구독할 동아리의 ID
     * @return 정상 처리 시 동아리의 구독자 수를 [Result]에 담아 반환. 실패 시 [Result.Failure]
     */
    suspend fun subscribeClub(clubId: Int): Result<Int>

    /**
     * 동아리를 구독 해제한다.
     *
     * @param clubId 구독 해제할 동아리의 ID
     * @return 정상 처리 시 동아리의 구독자 수를 [Result]에 담아 반환. 실패 시 [Result.Failure]
     */
    suspend fun unsubscribeClub(clubId: Int): Result<Int>

    /**
     * 동아리의 상세 정보를 가져온다.
     *
     * @param clubId 동아리 ID
     * @return 정상 처리 시 동아리의 상세 정보를 [Result]에 담아 반환. 실패 시 [Result.Failure]
     */
    suspend fun getClubDetail(clubId: Int): Result<Club>

    /**
     * 필터 조건에 맞는 동아리 목록을 페이징으로 가져온다.
     *
     * @param category 동아리 카테고리
     * @param division 동아리 구분
     * @param sortBy 정렬 기준
     * @return 동아리 목록의 [PagingData]를 [Flow]로 반환
     */
    fun getClubs(
        category: ClubCategory,
        division: Set<ClubDivision>,
        sortBy: String,
    ): Flow<PagingData<ClubSummary>>

    /**
     * 구독한 동아리 목록을 페이징으로 가져온다.
     *
     * @return 동아리 목록의 [PagingData]를 [Flow]로 반환
     */
    fun getSubscribedClubs(): Flow<PagingData<ClubSummary>>
}
