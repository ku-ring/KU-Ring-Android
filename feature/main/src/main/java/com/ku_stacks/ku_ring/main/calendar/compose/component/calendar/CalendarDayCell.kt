package com.ku_stacks.ku_ring.main.calendar.compose.component.calendar

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.AcademicEvent
import com.ku_stacks.ku_ring.main.calendar.model.DayModel
import com.ku_stacks.ku_ring.main.calendar.type.ScheduleType
import com.ku_stacks.ku_ring.main.calendar.type.color
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun CalendarDayCell(
    dayModel: DayModel,
    onClick: () -> Unit,
    events: ImmutableList<AcademicEvent>,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    val calendarColor = CalendarDefaults.calendarColor()
    val backgroundColor = with(calendarColor) {
        animateColorAsState(
            targetValue = when {
                dayModel.isOutDate -> outDateContainerColor
                isSelected -> selectedMarkerColor
                dayModel.isToday -> todayContainerColor
                else -> containerColor
            },
            label = "CalendarDayCell backgroundColor"
        )
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
                    color = backgroundColor.value,
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

        EventIndicators(
            events = events,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun EventIndicators(
    events: ImmutableList<AcademicEvent>,
    modifier: Modifier = Modifier,
) {
    val maxIndicatorCount = 6
    val indicatedEvents = events.take(maxIndicatorCount)

    Row(modifier = modifier) {
        indicatedEvents.forEachIndexed { index, event ->
            val color = ScheduleType.Companion.from(event.category).color()
            val shape =
                when (index) {
                    0 -> RoundedCornerShape(
                        topStart = 5.dp,
                        bottomStart = 5.dp,
                    )

                    indicatedEvents.lastIndex -> RoundedCornerShape(
                        topEnd = 5.dp,
                        bottomEnd = 5.dp,
                    )

                    else -> RectangleShape
                }


            Box(
                modifier = Modifier
                    .size(5.dp)
                    .background(
                        color = color,
                        shape = shape,
                    )
            )
        }
    }
}