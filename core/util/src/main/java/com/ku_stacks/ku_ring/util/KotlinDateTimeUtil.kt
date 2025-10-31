package com.ku_stacks.ku_ring.util

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
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

fun LocalDate.getMondayAndSundayOfWeek(): Pair<LocalDate, LocalDate> {
    val dayOfWeek = dayOfWeek.isoDayNumber
    val monday = minus(dayOfWeek - 1, DateTimeUnit.DAY)
    val sunday = plus(6, DateTimeUnit.DAY)
    return monday to sunday
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