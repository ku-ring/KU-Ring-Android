package com.ku_stacks.ku_ring.domain.club

import com.ku_stacks.ku_ring.domain.Club
import com.ku_stacks.ku_ring.domain.ClubCategory
import com.ku_stacks.ku_ring.domain.ClubDivision
import com.ku_stacks.ku_ring.domain.ClubSummary

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
     * 필터 조건에 맞는 동아리 목록을 가져온다.
     *
     * @param category 동아리 카테고리
     * @param division 동아리 구분
     * @return 정상 처리 시 동아리 목록을 [Result]에 담아 반환. 실패 시 [Result.Failure]
     */
    suspend fun getClubs(
        category: ClubCategory,
        division: Set<ClubDivision>,
    ): Result<List<ClubSummary>>

    /**
     * 구독한 동아리 목록을 가져온다.
     *
     * @return 정상 처리 시 동아리 목록을 [Result]에 담아 반환. 실패 시 [Result.Failure]
     */
    suspend fun getSubscribedClubs(): Result<List<ClubSummary>>
}
