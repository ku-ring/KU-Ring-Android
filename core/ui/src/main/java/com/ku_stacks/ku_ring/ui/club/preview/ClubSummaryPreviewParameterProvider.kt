package com.ku_stacks.ku_ring.ui.club.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.ku_stacks.ku_ring.domain.ClubCategory
import com.ku_stacks.ku_ring.domain.ClubDivision
import com.ku_stacks.ku_ring.domain.ClubSummary
import com.ku_stacks.ku_ring.util.now
import kotlinx.datetime.LocalDateTime

class ClubSummaryPreviewParameterProvider : PreviewParameterProvider<List<ClubSummary>> {
    val dummy = ClubSummary(
        id = 1,
        name = "쿠링 학술 소모임",
        summary = "안드로이드를 공부하는 모임입니다.",
        category = ClubCategory.ACADEMIC,
        division = ClubDivision.ENGINEERING,
        posterImageUrl = null,
        isSubscribed = false,
        subscribeCount = 1,
        recruitmentStart = LocalDateTime(2026, 2, 25, 0, 0),
        recruitmentEnd = LocalDateTime.now(),
    )

    override val values: Sequence<List<ClubSummary>>
        get() = sequenceOf(
            listOf(
                dummy,
                dummy.copy(id = 2),
                dummy.copy(id = 3),
            ),
            emptyList(),
        )
}
