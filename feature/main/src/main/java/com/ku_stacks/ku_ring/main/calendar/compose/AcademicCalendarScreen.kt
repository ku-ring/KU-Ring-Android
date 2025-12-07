package com.ku_stacks.ku_ring.main.calendar.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ku_stacks.ku_ring.designsystem.R.color.kus_label
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.indicator.PagingLoadingIndicator
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.AcademicEvent
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.calendar.AcademicCalendarUiState
import com.ku_stacks.ku_ring.main.calendar.AcademicCalendarViewModel
import com.ku_stacks.ku_ring.main.calendar.AcademicEventLoadState
import com.ku_stacks.ku_ring.main.calendar.compose.component.AcademicScheduleItem
import com.ku_stacks.ku_ring.main.calendar.compose.component.calendar.CalendarHeader
import com.ku_stacks.ku_ring.main.calendar.compose.component.calendar.CalendarMonthSection
import com.ku_stacks.ku_ring.main.calendar.compose.component.calendar.CalendarWeekdaysHeader
import com.ku_stacks.ku_ring.main.calendar.compose.component.calendar.MonthCalendarState
import com.ku_stacks.ku_ring.main.calendar.compose.component.calendar.rememberMonthCalendarState
import com.ku_stacks.ku_ring.main.calendar.model.DayModel
import com.ku_stacks.ku_ring.main.calendar.type.DayOwner
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
fun AcademicCalendarScreen(
    modifier: Modifier = Modifier,
    viewModel: AcademicCalendarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val calendarState = rememberMonthCalendarState()

    LaunchedEffect(calendarState.currentMonthModel) {
        snapshotFlow { calendarState.currentMonthModel }
            .distinctUntilChanged()
            .collect { monthModel ->
                val visibleDateRange = monthModel.visibleDateRange
                viewModel.fetchAcademicEvents(visibleDateRange.start, visibleDateRange.endInclusive)
                viewModel.updateSelectedDateOnScroll(monthModel)
            }
    }

    LaunchedEffect(uiState.selectedDate) {
        val currentPage = calendarState.pagerState.currentPage
        when (uiState.selectedDate.type) {
            DayOwner.PREVIOUS_MONTH -> calendarState.pagerState.animateScrollToPage(currentPage - 1)
            DayOwner.NEXT_MONTH -> calendarState.pagerState.animateScrollToPage(currentPage + 1)
            DayOwner.CURRENT_MONTH -> {}
        }
    }

    AcademicCalendarScreen(
        uiState = uiState,
        calendarState = calendarState,
        onDateClick = viewModel::updateSelectedDateOnClick,
        modifier = modifier,
    )
}

@Composable
private fun AcademicCalendarScreen(
    uiState: AcademicCalendarUiState,
    calendarState: MonthCalendarState,
    onDateClick: (DayModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxSize()
            .background(color = KuringTheme.colors.background),
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        CalendarHeader(
            text = calendarState.currentMonthModel.toString(),
            contentPadding = PaddingValues(horizontal = 20.dp),
            onChevronClick = { pageChange ->
                val currentPage = calendarState.pagerState.currentPage
                coroutineScope.launch {
                    calendarState.pagerState.animateScrollToPage(currentPage + pageChange.value)
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
            monthEvents = uiState.monthEvent,
            onDateClick = onDateClick,
        )

        HorizontalDivider(
            thickness = 2.dp, color = KuringTheme.colors.borderline,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 16.dp),
        )

        when (uiState.loadState) {
            AcademicEventLoadState.Success -> {
                AcademicScheduleColumn(
                    academicEvents = uiState.eventsOnSelectedDate,
                    modifier = Modifier.weight(1f),
                )
            }

            AcademicEventLoadState.Loading -> {
                PagingLoadingIndicator(Modifier.fillMaxSize())
            }

            AcademicEventLoadState.Error -> {
                LoadingErrorText(Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun CalendarPager(
    selectedDate: DayModel,
    calendarState: MonthCalendarState,
    monthEvents: ImmutableMap<String, List<AcademicEvent>>,
    onDateClick: (DayModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = calendarState.pagerState,
        contentPadding = PaddingValues(horizontal = 20.dp),
        pageSpacing = 74.dp,
        modifier = modifier.aspectRatio(1.2f),
    ) { page ->
        CalendarMonthSection(
            month = calendarState.getMonthModel(page),
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

@Composable
private fun LoadingErrorText(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.calendar_error_load_event),
            style = KuringTheme.typography.caption1,
            color = colorResource(id = kus_label),
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center,
        )
    }
}

@LightAndDarkPreview
@Composable
private fun AcademicCalendarScreenPreview() {
    KuringTheme {
        AcademicCalendarScreen(
            uiState = AcademicCalendarUiState.Empty.copy(
                loadState = AcademicEventLoadState.Error,
            ),
            calendarState = rememberMonthCalendarState(),
            onDateClick = { }
        )
    }
}
