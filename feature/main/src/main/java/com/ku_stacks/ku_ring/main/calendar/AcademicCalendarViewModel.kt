package com.ku_stacks.ku_ring.main.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.domain.academicevent.usecase.GetAcademicEventsUseCase
import com.ku_stacks.ku_ring.main.calendar.model.DayModel
import com.ku_stacks.ku_ring.main.calendar.model.MonthModel
import com.ku_stacks.ku_ring.main.calendar.type.DayOwner
import com.ku_stacks.ku_ring.util.now
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.yearMonth
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AcademicCalendarViewModel @Inject constructor(
    private val getAcademicEventsUseCase: GetAcademicEventsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AcademicCalendarUiState.Empty)
    internal val uiState = _uiState.asStateFlow()

    internal fun fetchAcademicEvents(startDate: LocalDate, endDate: LocalDate) =
        viewModelScope.launch {
            getAcademicEventsUseCase(startDate, endDate)
                .onSuccess {
                    _uiState.update { currentState ->
                        currentState.copy(
                            monthEvent = it.toImmutableMap(),
                            loadState = AcademicEventLoadState.Success,
                        )
                    }
                }
                .onFailure { t ->
                    Timber.e(t)
                    _uiState.update { it.copy(loadState = AcademicEventLoadState.Error) }
                }
        }

    internal fun updateSelectedDateOnClick(date: DayModel) {
        _uiState.update { currentState ->
            currentState.copy(selectedDate = date)
        }
    }

    internal fun updateSelectedDateOnScroll(monthModel: MonthModel) {
        // 선택된 날짜가 현재 페이지에 속하지 않는 경우에만 날짜를 초기화합니다.
        if (uiState.value.selectedDate.date.yearMonth == monthModel.yearMonth) return

        val date = with(monthModel) {
            if (yearMonth == referenceDate.yearMonth) LocalDate.now()
            else yearMonth.firstDay
        }

        _uiState.update { currentState ->
            currentState.copy(
                selectedDate = DayModel(
                    date = date,
                    isToday = date == LocalDate.now(),
                    type = DayOwner.CURRENT_MONTH,
                )
            )
        }
    }
}
