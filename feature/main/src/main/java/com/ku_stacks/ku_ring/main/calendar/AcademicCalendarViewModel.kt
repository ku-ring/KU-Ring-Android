package com.ku_stacks.ku_ring.main.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.domain.AcademicEvent
import com.ku_stacks.ku_ring.domain.academicevent.usecase.GetAcademicEventsUseCase
import com.ku_stacks.ku_ring.main.calendar.type.ScheduleType
import com.ku_stacks.ku_ring.util.now
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.YearMonth
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AcademicCalendarViewModel @Inject constructor(
    private val getAcademicEventsUseCase: GetAcademicEventsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AcademicCalendarUiState.Empty)
    internal val uiState = _uiState.asStateFlow()

    internal fun fetchAcademicEvents(yearMonth: YearMonth) = viewModelScope.launch {
        getAcademicEventsUseCase(yearMonth.firstDay, yearMonth.lastDay)
            .onSuccess {
                val eventMap = it.toImmutableMap()
                updateEventLoadState(AcademicEventLoadState.Success(eventMap))
            }
            .onFailure { t ->
                Timber.e(t)
                updateEventLoadState(
                    AcademicEventLoadState.Error(t.message.toString())
                )
            }
    }

    internal fun updateSelectedDate(date: LocalDate) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedDate = date
            )
        }
    }

    private fun updateEventLoadState(newValue: AcademicEventLoadState) {
        _uiState.update { currentState ->
            currentState.copy(
                eventLoadState = newValue
            )
        }
    }
}

// TODO: API 연결 후 삭제
internal val mockEvents = buildList {
    repeat(10) { index ->
        val start = LocalDateTime.now()
        val end = LocalDateTime
            .now().date
            .plus(DatePeriod(days = 1))
            .atTime(0, 0)

        add(
            AcademicEvent(
                id = index.toLong(),
                summary = "수강바구니 ${index}차",
                category = ScheduleType.EVENT.name,
                startDateTime = start,
                endDateTime = start,
            )
        )

        add(
            AcademicEvent(
                id = index.toLong() + 10,
                summary = "수강바구니 ${index.toLong() + 10}차",
                category = ScheduleType.ETC.name,
                startDateTime = end,
                endDateTime = end,
            )
        )
    }
}.groupBy { LocalDateTime.parse(it.startDateTime.toString()).date.toString() }.toImmutableMap()
