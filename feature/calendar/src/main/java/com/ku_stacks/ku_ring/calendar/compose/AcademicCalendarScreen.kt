package com.ku_stacks.ku_ring.calendar.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.calendar.compose.component.AcademicCalendarTopBar
import com.ku_stacks.ku_ring.calendar.compose.component.AcademicScheduleItem
import com.ku_stacks.ku_ring.calendar.compose.component.CalendarHeader
import com.ku_stacks.ku_ring.calendar.compose.component.CalendarMonthSection
import com.ku_stacks.ku_ring.calendar.compose.component.CalendarWeekdaysHeader
import com.ku_stacks.ku_ring.calendar.state.MonthCalendarState
import com.ku_stacks.ku_ring.calendar.state.rememberMonthCalendarState
import com.ku_stacks.ku_ring.calendar.type.ScheduleType
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.util.now
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@Composable
internal fun AcademicCalendarRoute(
    modifier: Modifier = Modifier,
) {
    val calendarState = rememberMonthCalendarState()
    val selectedDate = rememberSaveable { mutableStateOf(LocalDate.now()) }

    AcademicCalendarScreen(
        selectedDate = selectedDate.value,
        calendarState = calendarState,
        onDateClick = { date -> selectedDate.value = date },
        modifier = modifier,
    )
}

@Composable
private fun AcademicCalendarScreen(
    selectedDate: LocalDate,
    calendarState: MonthCalendarState,
    modifier: Modifier = Modifier,
    onDateClick: (LocalDate) -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = KuringTheme.colors.background),
    ) {
        AcademicCalendarTopBar()

        Spacer(modifier = Modifier.height(16.dp))

        CalendarHeader(
            text = calendarState.currentMonthModel.toString(),
            onChevronClick = { pageChange ->
                val currentPage = calendarState.currentPage
                coroutineScope.launch {
                    calendarState.animateScrollToPage(currentPage + pageChange)
                }
            },
        )

        Spacer(modifier = Modifier.height(10.dp))

        CalendarWeekdaysHeader(
            contentPadding = PaddingValues(horizontal = 20.dp),
        )

        Spacer(modifier = Modifier.height(4.dp))

        CalendarPager(
            calendarState = calendarState,
            selectedDate = selectedDate,
            onDateClick = onDateClick,
        )

        HorizontalDivider(
            thickness = 2.dp, color = KuringTheme.colors.borderline,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 16.dp),
        )

        AcademicScheduleColumn(
            academicSchedules = mockAcademicSchedules,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun CalendarPager(
    selectedDate: LocalDate,
    calendarState: MonthCalendarState,
    onDateClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = calendarState,
        contentPadding = PaddingValues(horizontal = 20.dp),
        pageSpacing = 74.dp,
        modifier = modifier.wrapContentSize(),
    ) { page ->
        CalendarMonthSection(
            month = calendarState.currentMonthModel,
            selectedDate = selectedDate,
            onDateClick = onDateClick,
        )
    }
}

@Composable
private fun AcademicScheduleColumn(
    academicSchedules: List<Triple<String, String, ScheduleType>>, // TODO: 도메인 모델로 교체
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 28.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier,
    ) {
        items(academicSchedules) { (schedule, period, type) ->
            AcademicScheduleItem(
                title = schedule,
                period = period,
                scheduleType = type,
            )
        }
    }
}

private val mockAcademicSchedules = buildList {
    repeat(10) { index ->
        add(
            Triple(
                "수강바구니 ${index}차",
                "8. 04 (월) 오전 9:30 - 8. 05 (화) 오후 5:00",
                ScheduleType.entries[index % ScheduleType.entries.size]
            )
        )
    }
}

@LightAndDarkPreview
@Composable
private fun AcademicCalendarScreenPreview() {
    KuringTheme {
        AcademicCalendarRoute()
    }
}
