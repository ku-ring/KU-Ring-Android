package com.ku_stacks.ku_ring.main.calendar.model

import androidx.compose.runtime.Immutable
import com.ku_stacks.ku_ring.main.calendar.type.DayOwner
import kotlinx.datetime.LocalDate

@Immutable
data class DayModel(
    val date: LocalDate,
    val isToday: Boolean,
    val type: DayOwner,
) {
    // LocalDate의 기본 파싱 형식: yyyy-MM-dd
    val mapKey: String = date.toString()
    val isOutDate = type != DayOwner.CURRENT_MONTH
}
