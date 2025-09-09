package com.ku_stacks.ku_ring.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.calendar.type.ScheduleType
import com.ku_stacks.ku_ring.domain.AcademicEvent
import com.ku_stacks.ku_ring.util.now
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.YearMonth
import javax.inject.Inject

@HiltViewModel
class AcademicCalendarViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(AcademicCalendarUiState.Empty)
    internal val uiState = _uiState.asStateFlow()

    /*
     * TODO: API 요청 로직 연결
     * yearMonth의 firstDay, lastDay를 사용해 API 요청
     * 요청 결과는 LocalDate의 문자열 키로 매핑 (파일 하단의 mockEvents 참고)
     */
    internal fun fetchAcademicEvents(yearMonth: YearMonth) = viewModelScope.launch {
        updateEventLoadState(AcademicEventLoadState.Success(mockEvents))
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
        val end = LocalDateTime.now()

        add(
            AcademicEvent(
                id = index.toLong(),
                summary = "수강바구니 ${index}차",
                category = ScheduleType.EVENT.name,
                startTime = start.toString(),
                endTime = start.toString(),
            )
        )

        add(
            AcademicEvent(
                id = index.toLong() + 10,
                summary = "수강바구니 ${index.toLong() + 10}차",
                category = ScheduleType.ETC.name,
                startTime = end.toString(),
                endTime = end.toString(),
            )
        )
    }
}.groupBy { LocalDateTime.parse(it.startTime).date.toString() }.toImmutableMap()
