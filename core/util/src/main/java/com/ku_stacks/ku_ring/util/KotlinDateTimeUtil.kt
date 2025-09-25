package com.ku_stacks.ku_ring.util

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.yearMonth
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun LocalDateTime.Companion.now(): LocalDateTime {
    val currentTime: Instant = Clock.System.now()
    val now: LocalDateTime = currentTime.toLocalDateTime(TimeZone.currentSystemDefault())
    return now
}

fun LocalDate.Companion.now(): LocalDate {
    val now = LocalDateTime.now()
    return now.date
}

fun DayOfWeek.koreanDayOfWeek(long: Boolean = false): String {
    var text: String = when(this) {
        DayOfWeek.MONDAY -> "월"
        DayOfWeek.TUESDAY -> "화"
        DayOfWeek.WEDNESDAY -> "수"
        DayOfWeek.THURSDAY -> "목"
        DayOfWeek.FRIDAY -> "금"
        DayOfWeek.SATURDAY -> "토"
        DayOfWeek.SUNDAY -> "일"
    }
    if (long) text += "요일"
    return text
}

fun YearMonth.Companion.now(): YearMonth {
    val today = LocalDate.now()
    val currentYearMonth = today.yearMonth
    return currentYearMonth
}