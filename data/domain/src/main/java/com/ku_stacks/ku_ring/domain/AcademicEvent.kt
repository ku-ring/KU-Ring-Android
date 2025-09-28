package com.ku_stacks.ku_ring.domain

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number

data class AcademicEvent(
    val id: Long,
    val summary: String,
    val category: String,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
) {
    /**
     * "**월. 일 (요일) 오전/오후 시:분**" 형식으로 시작 날짜와 종료 날짜를 표시하는 문자열
     */
    val period: String
        get() = runCatching {
            val formattedStart = formatLocalDateTime(startDateTime)
            val formattedEnd = formatLocalDateTime(endDateTime)
            "$formattedStart - $formattedEnd"
        }.getOrElse {
            "$startDateTime - $endDateTime"
        }

    private fun formatLocalDateTime(dateTime: LocalDateTime): String = runCatching {
        val month = dateTime.month.number
        val date = dateTime.day.toString().padStart(2, '0')
        val minute = dateTime.minute.toString().padStart(2, '0')
        val koreanDayOfWeek = getKoreanDayOfWeek(dateTime.dayOfWeek)
        val amPm = if (dateTime.hour >= 12) "오후" else "오전"

        val hour12 = with(dateTime.hour % 12) { if (this == 0) 12 else this }
        val hour = hour12.toString().padStart(2, '0')

        "$month. $date ($koreanDayOfWeek) $amPm $hour:$minute"
    }.getOrDefault("")

    private fun getKoreanDayOfWeek(dayOfWeek: DayOfWeek): String =
        when (dayOfWeek) {
            DayOfWeek.MONDAY -> "월"
            DayOfWeek.TUESDAY -> "화"
            DayOfWeek.WEDNESDAY -> "수"
            DayOfWeek.THURSDAY -> "목"
            DayOfWeek.FRIDAY -> "금"
            DayOfWeek.SATURDAY -> "토"
            DayOfWeek.SUNDAY -> "일"
        }
}

