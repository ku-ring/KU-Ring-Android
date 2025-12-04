package com.ku_stacks.ku_ring.main.calendar.model

import androidx.compose.runtime.Immutable
import com.ku_stacks.ku_ring.util.now
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateRange
import kotlinx.datetime.YearMonth
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.yearMonth

@Immutable
data class MonthModel(
    val yearMonth: YearMonth,
    val referenceDate: LocalDate = LocalDate.now()
) {
    val visibleDateRange: LocalDateRange = calculateVisibleDateRange()
    val calendarMonth: List<List<DayModel>> = generateCalendarGrid()

    private fun calculateVisibleDateRange(): LocalDateRange {
        val startOfMonth = yearMonth.firstDay
        val endOfMonth = yearMonth.lastDay

        val inDays = startOfMonth.dayOfWeek.isoDayNumber % 7
        val daysFilled = inDays + endOfMonth.day
        val outDays = (7 - (daysFilled % 7)) % 7

        val firstDayOnCalendar = startOfMonth.minus(DatePeriod(days = inDays))
        val lastDayOnCalendar = endOfMonth.plus(DatePeriod(days = outDays))

        return firstDayOnCalendar .. lastDayOnCalendar
    }

    private fun generateCalendarGrid(): List<List<DayModel>> {
        val firstDay = visibleDateRange.start
        val lastDay = visibleDateRange.endInclusive
        val totalDays = (lastDay.toEpochDays() - firstDay.toEpochDays()).toInt() + 1

        return (0 until totalDays).chunked(7) { week ->
            week.map { dayOffset ->
                val date = firstDay.plus(DatePeriod(days = dayOffset))
                DayModel(
                    date = date,
                    isToday = date == referenceDate,
                    isOutDate = date.yearMonth != yearMonth
                )
            }
        }
    }

    override fun toString(): String {
        val yearString = yearMonth.year
        val monthString = yearMonth.month.number
        return STRING_FORMAT.format(monthString, yearString)
    }

    companion object {
        private const val STRING_FORMAT = "%s월 %s년"
    }
}