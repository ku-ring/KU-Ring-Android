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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ku_stacks.ku_ring.calendar.AcademicCalendarUiState
import com.ku_stacks.ku_ring.calendar.AcademicCalendarViewModel
import com.ku_stacks.ku_ring.calendar.AcademicEventLoadState
import com.ku_stacks.ku_ring.calendar.compose.component.AcademicCalendarTopBar
import com.ku_stacks.ku_ring.calendar.compose.component.AcademicScheduleItem
import com.ku_stacks.ku_ring.calendar.compose.component.calendar.CalendarHeader
import com.ku_stacks.ku_ring.calendar.compose.component.calendar.CalendarMonthSection
import com.ku_stacks.ku_ring.calendar.compose.component.calendar.CalendarWeekdaysHeader
import com.ku_stacks.ku_ring.calendar.compose.component.calendar.MonthCalendarState
import com.ku_stacks.ku_ring.calendar.compose.component.calendar.rememberMonthCalendarState
import com.ku_stacks.ku_ring.calendar.mockEvents
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.AcademicEvent
import com.ku_stacks.ku_ring.util.now
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@Composable
internal fun AcademicCalendarRoute(
    modifier: Modifier = Modifier,
    viewModel: AcademicCalendarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val calendarState = rememberMonthCalendarState()

    LaunchedEffect(calendarState.currentPage) {
        snapshotFlow { calendarState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                val yearMonth = calendarState.getYearMonth(page)
                viewModel.fetchAcademicEvents(yearMonth)
            }
    }

    AcademicCalendarScreen(
        uiState = uiState,
        calendarState = calendarState,
        onDateClick = viewModel::updateSelectedDate,
        modifier = modifier,
    )
}

@Composable
private fun AcademicCalendarScreen(
    uiState: AcademicCalendarUiState,
    calendarState: MonthCalendarState,
    onDateClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val monthEvents = remember {
        derivedStateOf {
            val loadState = uiState.eventLoadState as? AcademicEventLoadState.Success
            loadState?.eventMap ?: persistentMapOf()
        }
    }

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
            selectedDate = uiState.selectedDate,
            monthEvents = monthEvents.value,
            onDateClick = onDateClick,
        )

        HorizontalDivider(
            thickness = 2.dp, color = KuringTheme.colors.borderline,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 16.dp),
        )

        AcademicScheduleColumn(
            academicEvents = uiState.eventsOnSelectedDate,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun CalendarPager(
    selectedDate: LocalDate,
    calendarState: MonthCalendarState,
    monthEvents: ImmutableMap<String, List<AcademicEvent>>,
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
            monthEvents = monthEvents,
            selectedDate = selectedDate,
            onDateClick = onDateClick,
        )
    }
}

@Composable
private fun AcademicScheduleColumn(
    academicEvents: ImmutableList<AcademicEvent>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 28.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier,
    ) {
        items(
            items = academicEvents,
            key = { it.id },
        ) { event ->
            AcademicScheduleItem(
                event = event,
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun AcademicCalendarScreenPreview() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    KuringTheme {
        AcademicCalendarScreen(
            uiState = AcademicCalendarUiState(
                selectedDate = selectedDate,
                eventLoadState = AcademicEventLoadState.Success(mockEvents),
            ),
            calendarState = rememberMonthCalendarState(),
            onDateClick = { date -> selectedDate = date }
        )
    }
}
