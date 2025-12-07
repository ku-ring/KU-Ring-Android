package com.ku_stacks.ku_ring.main.calendar

import com.ku_stacks.ku_ring.main.calendar.model.MonthModel
import com.ku_stacks.ku_ring.util.now
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.YearMonth
import kotlinx.datetime.plus
import kotlinx.datetime.yearMonth
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.IntStream

class MonthModelTest {

    companion object {
        @JvmStatic
        fun monthRangeProvider(): IntStream {
            return IntStream.range(-20, 21)
        }
    }

    @ParameterizedTest
    @MethodSource("monthRangeProvider")
    fun `calendarMonth's number of days is a multiple of 7`(monthOffset: Int) {
        // given
        val yearMonth = YearMonth.now().plus(monthOffset, DateTimeUnit.MONTH)
        val monthModel = MonthModel(yearMonth)

        // when
        val monthModelSize = monthModel.calendarMonth.flatten().size

        // then
        assertEquals(0, monthModelSize % 7)
    }

    @ParameterizedTest
    @MethodSource("monthRangeProvider")
    fun `visibleDateRange must start on Sunday and end on Saturday`(monthOffset: Int) {
        // given
        val yearMonth = YearMonth.now().plus(monthOffset, DateTimeUnit.MONTH)
        val monthModel = MonthModel(yearMonth)

        // when
        val visibleDateRange = monthModel.visibleDateRange
        val start = visibleDateRange.start.dayOfWeek
        val end = visibleDateRange.endInclusive.dayOfWeek

        // then
        assertEquals(DayOfWeek.SUNDAY, start)
        assertEquals(DayOfWeek.SATURDAY, end)
    }

    @ParameterizedTest
    @MethodSource("monthRangeProvider")
    fun `visibleDateRange is not more than a week far from month`(monthOffset: Int) {
        // given
        val yearMonth = YearMonth.now().plus(monthOffset, DateTimeUnit.MONTH)
        val monthModel = MonthModel(yearMonth)

        // when
        val visibleDateRange = monthModel.visibleDateRange
        val start = visibleDateRange.start
        val end = visibleDateRange.endInclusive

        val startDiff = (yearMonth.firstDay.toEpochDays() - start.toEpochDays()).toInt()
        val endDiff = (end.toEpochDays() - yearMonth.lastDay.toEpochDays()).toInt()

        // then
        assertTrue(startDiff < 7)
        assertTrue(endDiff < 7)
    }

    @ParameterizedTest
    @MethodSource("monthRangeProvider")
    fun `calendar distinguishes previous month's and next month's days correctly`(monthOffset: Int) {
        // given
        val yearMonth = YearMonth.now().plus(monthOffset, DateTimeUnit.MONTH)
        val monthModel = MonthModel(yearMonth)

        // when
        val dayModels = monthModel.calendarMonth.flatten()

        // then
        dayModels.forEach { dayModel ->
            if (dayModel.isOutDate) {
                assertTrue(dayModel.date.yearMonth != yearMonth)
            } else {
                assertTrue(dayModel.date.yearMonth == yearMonth)
            }
        }
    }
}