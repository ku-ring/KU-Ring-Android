package com.ku_stacks.ku_ring.calendar

import androidx.compose.runtime.Immutable
import com.ku_stacks.ku_ring.domain.AcademicEvent
import com.ku_stacks.ku_ring.util.now
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDate

@Immutable
internal data class AcademicCalendarUiState(
    val selectedDate: LocalDate,
    val eventLoadState: AcademicEventLoadState,
) {
    val eventsOnSelectedDate: ImmutableList<AcademicEvent>
        get() = runCatching {
            val loadState = eventLoadState as AcademicEventLoadState.Success
            requireNotNull(loadState.eventMap[selectedDate.toString()]).toImmutableList()
        }.getOrDefault(persistentListOf())

    companion object {
        val Empty = AcademicCalendarUiState(
            selectedDate = LocalDate.now(),
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
