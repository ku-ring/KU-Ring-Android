package com.ku_stacks.ku_ring.calendar.compose.component.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.calendar.mockEvents
import com.ku_stacks.ku_ring.calendar.model.MonthModel
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.AcademicEvent
import com.ku_stacks.ku_ring.util.now
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth

@Composable
internal fun CalendarMonthSection(
    month: MonthModel,
    selectedDate: LocalDate,
    monthEvents: ImmutableMap<String, List<AcademicEvent>>,
    onDateClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        month.calendarMonth.forEach { week ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                week.forEach { day ->
                    val key = day.date.toString()
                    val events = monthEvents[key] ?: emptyList()
                    CalendarDayCell(
                        dayModel = day,
                        isSelected = day.date == selectedDate,
                        events = events.toImmutableList(),
                        onClick = { onDateClick(day.date) },
                        modifier = Modifier.weight(1f)
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
    KuringTheme {
        CalendarMonthSection(
            month = monthModel,
            selectedDate = LocalDate.now(),
            onDateClick = {},
            monthEvents = mockEvents,
            modifier = Modifier.background(KuringTheme.colors.background)
        )
    }
}
