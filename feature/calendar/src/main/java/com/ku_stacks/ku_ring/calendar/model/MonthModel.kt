package com.ku_stacks.ku_ring.calendar.model

import androidx.compose.runtime.Immutable
import com.ku_stacks.ku_ring.util.now
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
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
    private val firstDayOfWeek = yearMonth.firstDay.dayOfWeek.isoDayNumber
    val inDays = firstDayOfWeek % 7
    val monthDays = yearMonth.lastDay.day
    val outDays = (7 - ((inDays + monthDays) % 7)) % 7
    val totalDays = monthDays + inDays + outDays
    private val rows = (0 until totalDays).chunked(7)
    val calendarMonth: List<List<DayModel>> = rows.map { week ->
        week.map { dayOffset ->
            getDay(dayOffset)
        }
    }

    private fun getDay(dayOffset: Int): DayModel {
        val firstDayOnCalendar = yearMonth.firstDay.minus(DatePeriod(days = inDays))
        val date = firstDayOnCalendar.plus(DatePeriod(days = dayOffset))
        val isOutDate = date.yearMonth != yearMonth
        val isToday = date == referenceDate
        return DayModel(date, isToday, isOutDate)
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