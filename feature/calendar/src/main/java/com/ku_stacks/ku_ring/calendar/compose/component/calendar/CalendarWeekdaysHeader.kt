package com.ku_stacks.ku_ring.calendar.compose.component.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.util.koreanDayOfWeek
import kotlinx.datetime.DayOfWeek

@Composable
fun CalendarWeekdaysHeader(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val calendarColor = CalendarDefaults.calendarColor()
    val dayOfWeekEntries = buildList {
        add(DayOfWeek.SUNDAY)
        addAll(DayOfWeek.entries.subList(0,6))
    }

    Row(
        modifier = modifier
            .background(calendarColor.containerColor)
            .heightIn(min = 18.dp)
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        dayOfWeekEntries.forEach { day ->
            val koreanDayOfWeek = day.koreanDayOfWeek()
            Text(
                modifier = Modifier.weight(1f),
                text = koreanDayOfWeek,
                style = KuringTheme.typography.tag,
                color = calendarColor.headerContentColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun CalendarWeekdaysHeaderPreview() {
    KuringTheme {
        CalendarWeekdaysHeader()
    }
}
