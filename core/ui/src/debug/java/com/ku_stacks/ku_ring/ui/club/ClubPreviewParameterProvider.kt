package com.ku_stacks.ku_ring.ui.club

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.ku_stacks.ku_ring.domain.ClubCategory
import com.ku_stacks.ku_ring.domain.ClubDivision
import com.ku_stacks.ku_ring.domain.ClubSummary
import com.ku_stacks.ku_ring.util.now
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus

class ClubItemCardPreviewParameterProvider : PreviewParameterProvider<ClubSummary> {
    fun LocalDateTime.plusDays(days: Int): LocalDateTime {
        val date = this.date.plus(days, DateTimeUnit.DAY)
        return LocalDateTime(date, this.time)
    }

    fun LocalDateTime.minusDays(days: Int): LocalDateTime {
        val date = this.date.minus(days, DateTimeUnit.DAY)
        return LocalDateTime(date, this.time)
    }

    val today = LocalDateTime.now()

    val summary = ClubSummary(
        id = 1,
        name = "{ClubName}",
        category = ClubCategory.ACADEMIC,
        division = ClubDivision.CENTRAL,
        summary = "{SubText}\n{SubText}",
        posterImageUrl = null,
        isSubscribed = false,
        subscribeCount = 10,
        recruitmentStart = today.minusDays(5),
        recruitmentEnd = today,
    )

    override val values: Sequence<ClubSummary>
        get() = sequenceOf(
            summary,
            summary.copy(recruitmentEnd = today.plusDays(5))
        )
}
