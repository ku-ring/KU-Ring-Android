package com.ku_stacks.ku_ring.main.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.domain.academicevent.usecase.GetAcademicEventsUseCase
import com.ku_stacks.ku_ring.main.calendar.model.DayModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AcademicCalendarViewModel @Inject constructor(
    private val getAcademicEventsUseCase: GetAcademicEventsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AcademicCalendarUiState.Empty)
    internal val uiState = _uiState.asStateFlow()

    internal fun fetchAcademicEvents(startDate: LocalDate, endDate: LocalDate) = viewModelScope.launch {
        updateEventLoadState(AcademicEventLoadState.Loading)
        getAcademicEventsUseCase(startDate, endDate)
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

    internal fun updateSelectedDate(date: DayModel) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedDate = date,
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
