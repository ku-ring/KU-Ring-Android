package com.ku_stacks.ku_ring.main.calendar

import androidx.compose.runtime.Immutable
import com.ku_stacks.ku_ring.domain.AcademicEvent
import com.ku_stacks.ku_ring.main.calendar.model.DayModel
import com.ku_stacks.ku_ring.main.calendar.type.DayType
import com.ku_stacks.ku_ring.util.now
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDate

@Immutable
internal data class AcademicCalendarUiState(
    val selectedDate: DayModel,
    val monthEvent: ImmutableMap<String, List<AcademicEvent>>,
    val loadState: AcademicEventLoadState,
) {
    val eventsOnSelectedDate: ImmutableList<AcademicEvent>
        get() = runCatching {
            requireNotNull(monthEvent[selectedDate.mapKey]).toImmutableList()
        }.getOrDefault(persistentListOf())

    companion object {
        val Empty = AcademicCalendarUiState(
            selectedDate = DayModel(
                date = LocalDate.now(),
                isToday = true,
                dayType = DayType.MONTH_DAY,
            ),
            monthEvent = persistentMapOf<String, List<AcademicEvent>>(),
            loadState = AcademicEventLoadState.Loading,
        )
    }
}

internal sealed interface AcademicEventLoadState {
    object Loading : AcademicEventLoadState
    object Success : AcademicEventLoadState
    object Error : AcademicEventLoadState
}
