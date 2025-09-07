package com.ku_stacks.ku_ring.calendar.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate

@Immutable
data class DayModel(
    val date: LocalDate,
    val isToday: Boolean,
    val isOutDate: Boolean,
) {
    // LocalDate의 기본 파싱 형식: yyyy-MM-dd
    val scrapMapKey: String = date.toString()
}
