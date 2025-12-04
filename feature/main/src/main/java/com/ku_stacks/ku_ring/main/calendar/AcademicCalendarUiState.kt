package com.ku_stacks.ku_ring.main.calendar

import androidx.compose.runtime.Immutable
import com.ku_stacks.ku_ring.domain.AcademicEvent
import com.ku_stacks.ku_ring.main.calendar.model.DayModel
import com.ku_stacks.ku_ring.main.calendar.type.DayType
import com.ku_stacks.ku_ring.util.now
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDate

@Immutable
internal data class AcademicCalendarUiState(
    val selectedDate: DayModel,
    val eventLoadState: AcademicEventLoadState,
) {
    val eventsOnSelectedDate: ImmutableList<AcademicEvent>
        get() = runCatching {
            val loadState = eventLoadState as AcademicEventLoadState.Success
            requireNotNull(loadState.eventMap[selectedDate.mapKey]).toImmutableList()
        }.getOrDefault(persistentListOf())

    companion object {
        val Empty = AcademicCalendarUiState(
            selectedDate = DayModel(
                date = LocalDate.now(),
                isToday = true,
                dayType = DayType.MONTH_DAY,
            ),
            eventLoadState = AcademicEventLoadState.Loading,
        )
    }
}

internal sealed interface AcademicEventLoadState {
    data object Loading : AcademicEventLoadState
    data class Error(val message: String) : AcademicEventLoadState
    data class Success(val eventMap: ImmutableMap<String, List<AcademicEvent>>) :
        AcademicEventLoadState
}
