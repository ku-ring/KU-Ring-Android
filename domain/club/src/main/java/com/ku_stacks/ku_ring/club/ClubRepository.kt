package com.ku_stacks.ku_ring.club

import com.ku_stacks.ku_ring.domain.Club

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
}