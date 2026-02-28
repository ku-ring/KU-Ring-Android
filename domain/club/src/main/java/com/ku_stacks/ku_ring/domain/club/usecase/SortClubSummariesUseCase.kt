package com.ku_stacks.ku_ring.domain.club.usecase

import com.ku_stacks.ku_ring.domain.ClubSummary
import com.ku_stacks.ku_ring.domain.isRecruitmentCompleted
import javax.inject.Inject

class SortClubSummariesUseCase @Inject constructor() {
    operator fun invoke(
        clubSummaries: List<ClubSummary>,
        sortOption: String,
    ): List<ClubSummary> {
        return clubSummaries
            .sortedWith(
                compareBy<ClubSummary> { it.isRecruitmentCompleted() } // 모집이 종료된 동아리 카드는 최하단에 정렬합니다,
                    .thenBy {
                        // TODO: SortOption을 도메인 모듈에 정의한 후 사용
                        when (sortOption) {
                            "ALPHABETIC" -> it.name
                            else -> it.recruitmentEnd
                        } as Comparable<*>?
                    }
            )
    }
}
