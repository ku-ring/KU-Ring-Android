package com.ku_stacks.ku_ring.calendar.compose.component.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.calendar.model.DayModel
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme

@Composable
internal fun CalendarDayCell(
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