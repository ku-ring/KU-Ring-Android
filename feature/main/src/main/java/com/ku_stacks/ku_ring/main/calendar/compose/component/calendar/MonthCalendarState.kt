package com.ku_stacks.ku_ring.main.calendar.compose.component.calendar

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import com.ku_stacks.ku_ring.main.calendar.model.MonthModel
import com.ku_stacks.ku_ring.util.now
import kotlinx.datetime.YearMonth
import kotlinx.datetime.number

/**
 * MonthCalendarState를 remember하는 함수.
 *
 * [MonthCalendarState]는 [PagerState]를 상속받고 있기 때문에, Pagers API와 호환이 가능합니다.
 *
 * ```
 * @Composable
 * fun MonthCalendar() {
 *      val calendarState = rememberMonthCalendarState()
 *      HorizontalPager(
 *          state = calendarState,
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
): MonthCalendarState = rememberSaveable(
    inputs = arrayOf<Any>(
        startYear,
        endYear,
        currentYearMonth,
    ),
    saver = MonthCalendarState.Saver
) {
    MonthCalendarState(
        startYear = startYear,
        endYear = endYear,
        currentYearMonth = currentYearMonth,
        pageCount = calculatePageCount(startYear, endYear),
    )
}

@Stable
internal class MonthCalendarState(
    currentYearMonth: YearMonth,
    val startYear: Int,
    val endYear: Int,
    override val pageCount: Int,
) : PagerState(
    currentPage = calculateCurrentPage(
        currentYear = currentYearMonth.year,
        currentMonth = currentYearMonth.month.number,
        startYear = startYear
    )
) {
    private var _currentYearMonth = derivedStateOf { getYearMonth(currentPage) }
    val currentYearMonth get() = _currentYearMonth.value
    private var _currentMonthModel = derivedStateOf { getMonthModel(currentPage) }
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

    companion object {
        val Saver: Saver<MonthCalendarState, *> = listSaver(
            save = {
                listOf(
                    it.startYear,
                    it.endYear,
                    it.currentYearMonth,
                    it.pageCount,
                )
            },
            restore = {
                MonthCalendarState(
                    startYear = it[0] as Int,
                    endYear = it[1] as Int,
                    currentYearMonth = (it[2] as YearMonth),
                    pageCount = it[3] as Int,
                )
            },
        )
    }
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