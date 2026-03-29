package com.ku_stacks.ku_ring.domain.club.usecase

import com.ku_stacks.ku_ring.domain.ClubSummary
import javax.inject.Inject

/**
 * 구독 여부 변경 시 [ClubSummary]의 구독 상태와 구독 수를 함께 반영합니다.
 *
 * 낙관적 업데이트(optimistic update)에 사용되며, 이전 구독 상태와 비교하여
 * [ClubSummary.subscribeCount]를 +1 / -1 / 유지 중 하나로 반영합니다.
 */
class ApplyClubSubscriptionUseCase @Inject constructor() {
    operator fun invoke(clubSummary: ClubSummary, isSubscribed: Boolean): ClubSummary {
        val countDelta = when {
            isSubscribed && !clubSummary.isSubscribed -> 1
            !isSubscribed && clubSummary.isSubscribed -> -1
            else -> 0
        }
        return clubSummary.copy(
            isSubscribed = isSubscribed,
            subscribeCount = (clubSummary.subscribeCount + countDelta).coerceAtLeast(0),
        )
    }
}
