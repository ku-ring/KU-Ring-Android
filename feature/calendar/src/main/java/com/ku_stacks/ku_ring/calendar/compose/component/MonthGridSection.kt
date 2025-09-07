package com.ku_stacks.ku_ring.calendar.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.calendar.model.CalendarDefaults
import com.ku_stacks.ku_ring.calendar.model.DayModel
import com.ku_stacks.ku_ring.calendar.model.MonthModel
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.util.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth

@Composable
internal fun MonthGridSection(
    month: MonthModel,
    selectedDate: LocalDate,
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
                    DayCell(
                        dayModel = day,
                        isSelected = day.date == selectedDate,
                        onClick = { onDateClick(day.date) },
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.DayCell(
    dayModel: DayModel,
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    val calendarColor = CalendarDefaults.calendarColor()
    val backgroundColor = with(calendarColor) {
        when {
            dayModel.isOutDate -> outDateContainerColor
            isSelected -> selectedMarkerColor
            dayModel.isToday -> todayContainerColor
            else -> containerColor
        }
    }

    val textColor = with(calendarColor) {
        when {
            dayModel.isOutDate -> outDateContentColor
            isSelected -> selectedContentColor
            dayModel.isToday -> todayContentColor
            else -> contentColor
        }
    }

    Box(
        modifier = modifier
            .weight(1f)
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .background(
                    color = backgroundColor,
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dayModel.date.day.toString(),
                color = textColor,
                style = KuringTheme.typography.date,
                textAlign = TextAlign.Center
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun CalendarSectionPreview() {
    val monthModel = MonthModel(YearMonth.now())
    KuringTheme {
        MonthGridSection(
            month = monthModel,
            selectedDate = LocalDate.now(),
            onDateClick = {},
            modifier = Modifier.background(KuringTheme.colors.background)
        )
    }
}
