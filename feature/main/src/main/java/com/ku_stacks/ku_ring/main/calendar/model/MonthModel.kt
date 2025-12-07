package com.ku_stacks.ku_ring.main.calendar.model

import androidx.compose.runtime.Immutable
import com.ku_stacks.ku_ring.main.calendar.type.DayOwner
import com.ku_stacks.ku_ring.util.now
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateRange
import kotlinx.datetime.YearMonth
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus

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

        val inDates = startOfMonth.dayOfWeek.isoDayNumber % 7
        val daysFilled = inDates + endOfMonth.day
        val outDates = (7 - (daysFilled % 7)) % 7

        val firstDayOnCalendar = startOfMonth.minus(DatePeriod(days = inDates))
        val lastDayOnCalendar = endOfMonth.plus(DatePeriod(days = outDates))

        return firstDayOnCalendar .. lastDayOnCalendar
    }

    private fun generateCalendarGrid(): List<List<DayModel>> {
        val firstDay = visibleDateRange.start
        val lastDay = visibleDateRange.endInclusive
        val totalDays = (lastDay.toEpochDays() - firstDay.toEpochDays()).toInt() + 1

        return (0 until totalDays).chunked(7) { week ->
            week.map { dayOffset ->
                val date = firstDay.plus(DatePeriod(days = dayOffset))
                val dayType = getDayType(date)
                DayModel(
                    date = date,
                    isToday = date == referenceDate,
                    type = dayType,
                )
            }
        }
    }

    private fun getDayType(date: LocalDate) = when  {
        date < yearMonth.firstDay -> DayOwner.PREVIOUS_MONTH
        date > yearMonth.lastDay -> DayOwner.NEXT_MONTH
        else -> DayOwner.CURRENT_MONTH
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