package com.ku_stacks.ku_ring.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

data class ClubSummary(
    val id: Int,
    val name: String,
    val summary: String,
    val category: ClubCategory,
    val division: ClubDivision,
    val posterImageUrl: String?,
    val isSubscribed: Boolean,
    val subscribeCount: Int,
    val recruitmentStart: LocalDateTime?,
    val recruitmentEnd: LocalDateTime?,
)

fun ClubSummary.isRecruitmentCompleted(): Boolean {
    val currentTime: Instant = Clock.System.now()
    val now: LocalDateTime = currentTime.toLocalDateTime(TimeZone.currentSystemDefault())
    return (recruitmentEnd?.compareTo(now) ?: 0) < 0
}

fun ClubSummary.calculateDDay(today: LocalDate): Int? =
    recruitmentEnd?.date?.let { endDate ->
        today.daysUntil(endDate).coerceAtLeast(0)
    }
