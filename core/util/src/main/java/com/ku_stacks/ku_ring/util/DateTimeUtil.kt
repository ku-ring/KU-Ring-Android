package com.ku_stacks.ku_ring.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneOffset

val LocalDateTime.yearMonth: YearMonth
    get() = YearMonth.of(this.year, this.monthValue)

fun Long.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochSecond(this), ZoneOffset.systemDefault())

fun LocalDateTime.toEpochSecond() = atZone(ZoneOffset.systemDefault()).toEpochSecond()

fun LocalDate.toEpochSecond() = atStartOfDay(ZoneOffset.systemDefault()).toEpochSecond()