package com.ku_stacks.ku_ring.main.calendar

import com.ku_stacks.ku_ring.main.calendar.model.MonthModel
import com.ku_stacks.ku_ring.util.now
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.YearMonth
import kotlinx.datetime.plus
import kotlinx.datetime.yearMonth
import org.junit.Assert.assertEquals
import org.junit.Test

class MonthModelTest {
    private val validationMonthRange = (-50 .. 50)

    @Test
    fun `calendarMonth's number of days is a multiple of 7`() {
        validationMonthRange.forEach { month ->
            // given
            val yearMonth = YearMonth.now().plus(month, DateTimeUnit.MONTH)
            val monthModel = MonthModel(yearMonth)

            // when
            val monthModelSize = monthModel.calendarMonth.flatten().size

            // then
            assertEquals(0, monthModelSize % 7)
        }
    }

    @Test
    fun `visibleDateRange is start on Sunday and end on Saturday`() {
        validationMonthRange.forEach { month ->
            // given
            val yearMonth = YearMonth.now().plus(month, DateTimeUnit.MONTH)
            val monthModel = MonthModel(yearMonth)

            // when
            val visibleDateRange = monthModel.visibleDateRange
            val start = visibleDateRange.start.dayOfWeek
            val end = visibleDateRange.endInclusive.dayOfWeek

            // then
            assertEquals(DayOfWeek.SUNDAY, start)
            assertEquals(DayOfWeek.SATURDAY, end)
        }
    }

    @Test
    fun `visibleDateRange is not more than a week far from month`() {
        validationMonthRange.forEach { month ->
            // given
            val yearMonth = YearMonth.now().plus(month, DateTimeUnit.MONTH)
            val monthModel = MonthModel(yearMonth)

            // when
            val visibleDateRange = monthModel.visibleDateRange
            val start = visibleDateRange.start
            val end = visibleDateRange.endInclusive

            val startDiff = (yearMonth.firstDay.toEpochDays() - start.toEpochDays()).toInt()
            val endDiff = (end.toEpochDays() - yearMonth.lastDay.toEpochDays()).toInt()

            println("start: $start, end: $end, month: $yearMonth")
            // then
            assert(startDiff < 7)
            assert(endDiff < 7)
        }
    }

    @Test
    fun `calendar distinguishes inDays and outDays correctly`() {
        validationMonthRange.forEach { month ->
            // given
            val yearMonth = YearMonth.now().plus(month, DateTimeUnit.MONTH)
            val monthModel = MonthModel(yearMonth)

            // when
            val dayModels = monthModel.calendarMonth.flatten()

            // then
            dayModels.forEach { dayModel ->
                if (dayModel.isOutDate) {
                    assert(dayModel.date.yearMonth != yearMonth)
                } else {
                    assert(dayModel.date.yearMonth == yearMonth)
                }
            }
        }
    }
}