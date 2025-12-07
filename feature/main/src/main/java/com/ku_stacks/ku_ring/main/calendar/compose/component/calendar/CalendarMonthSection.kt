package com.ku_stacks.ku_ring.main.calendar.compose.component.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.AcademicEvent
import com.ku_stacks.ku_ring.main.calendar.model.DayModel
import com.ku_stacks.ku_ring.main.calendar.model.MonthModel
import com.ku_stacks.ku_ring.main.calendar.type.DayOwner
import com.ku_stacks.ku_ring.util.now
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth

@Composable
internal fun CalendarMonthSection(
    month: MonthModel,
    selectedDate: DayModel,
    monthEvents: ImmutableMap<String, List<AcademicEvent>>,
    onDateClick: (DayModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        month.calendarMonth.forEach { week ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                week.forEach { day ->
                    val events = monthEvents[day.mapKey] ?: emptyList()
                    CalendarDayCell(
                        dayModel = day,
                        isSelected = day.date == selectedDate.date,
                        events = events.toImmutableList(),
                        onClick = { onDateClick(day) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@LightAndDarkPreview
@Composable
private fun CalendarMonthSectionPreview() {
    val monthModel = MonthModel(YearMonth.now())
    val dayModel = DayModel(LocalDate.now(), true, DayOwner.CURRENT_MONTH)
    KuringTheme {
        CalendarMonthSection(
            month = monthModel,
            selectedDate = dayModel,
            onDateClick = {},
            monthEvents = emptyMap<String, List<AcademicEvent>>().toImmutableMap(),
            modifier = Modifier.background(KuringTheme.colors.background),
        )
    }
}
