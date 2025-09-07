package com.ku_stacks.ku_ring.util

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.yearMonth
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun LocalDate.Companion.now(): LocalDate {
    val now: Instant = Clock.System.now()
    val today: LocalDate = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
    return today
}

fun DayOfWeek.koreanDayOfWeek(long: Boolean = false): String {
    val shortVersion = listOf("일", "월", "화", "수", "목", "금", "토")
    val text = StringBuilder(shortVersion[this.ordinal])
    if (long) text.append("요일")
    return text.toString()
}

fun YearMonth.Companion.now(): YearMonth {
    val today = LocalDate.now()
    val currentYearMonth = today.yearMonth
    return currentYearMonth
}