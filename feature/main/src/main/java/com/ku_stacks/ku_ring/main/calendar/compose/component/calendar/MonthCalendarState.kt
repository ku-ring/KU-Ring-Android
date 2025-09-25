package com.ku_stacks.ku_ring.main.calendar.compose.component.calendar

import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.ku_stacks.ku_ring.main.calendar.model.MonthModel
import com.ku_stacks.ku_ring.util.now
import kotlinx.datetime.YearMonth
import kotlinx.datetime.number

/**
 * MonthCalendarState를 remember하는 함수.
 *
 * 이 함수는 내부적으로 PagerState를 소유하여 Pagers API와 호환이 가능합니다.
 *
 * ```
 * @Composable
 * fun MonthCalendar() {
 *      val calendarState = rememberMonthCalendarState()
 *      HorizontalPager(
 *          state = calendarState.pagerState,
 *      ) { page ->
 *          // contents
 *      }
 * }
 * ```
 *
 * @param startYear the first year of the calendar
 * @param endYear the last year of the calendar
 * @param currentYearMonth the current year and month, which will be initially displayed in the calendar
 *
 * @return [MonthCalendarState]
 */

@Composable
internal fun rememberMonthCalendarState(
    currentYearMonth: YearMonth = YearMonth.now(),
    startYear: Int = currentYearMonth.year - CalendarDefaults.YEAR_RANGE,
    endYear: Int = currentYearMonth.year + CalendarDefaults.YEAR_RANGE,
): MonthCalendarState {
    val pagerState = rememberPagerState(
        initialPage = calculateCurrentPage(
            currentYear = currentYearMonth.year,
            currentMonth = currentYearMonth.month.number,
            startYear = startYear
        ),
        pageCount = { calculatePageCount(startYear, endYear) },
    )
    return remember {
        MonthCalendarState(
            pagerState = pagerState,
            startYear = startYear,
            endYear = endYear,
        )
    }
}

@Stable
internal class MonthCalendarState(
    val pagerState: PagerState,
    val startYear: Int,
    val endYear: Int,
) {
    private var _currentYearMonth = derivedStateOf { getYearMonth(pagerState.currentPage) }
    val currentYearMonth get() = _currentYearMonth.value
    private var _currentMonthModel = derivedStateOf { getMonthModel(pagerState.currentPage) }
    val currentMonthModel get() = _currentMonthModel.value

    /**
     * Returns the [YearMonth] for the given page.
     */
    fun getMonthModel(page: Int): MonthModel {
        val yearMonth = getYearMonth(page = page)
        return getMonthModel(year = yearMonth.year, month = yearMonth.month.number)
    }

    /**
     * Returns the [MonthModel] for the given YearMonth object.
     */
    fun getMonthModel(year: Int, month: Int): MonthModel {
        val yearMonth = YearMonth(year, month)
        return MonthModel(yearMonth = yearMonth)
    }

    /**
     * Returns the [YearMonth] for the given page.
     */
    fun getYearMonth(page: Int): YearMonth = YearMonth(
        startYear + page / 12,
        page % 12 + 1,
    )
}

private fun calculatePageCount(
    startYear: Int,
    endYear: Int,
) = (endYear - startYear + 1) * 12

private fun calculateCurrentPage(
    currentYear: Int,
    currentMonth: Int,
    startYear: Int
): Int = (currentYear - startYear) * 12 + (currentMonth - 1)