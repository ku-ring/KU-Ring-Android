package com.ku_stacks.ku_ring.main.calendar.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.AcademicEvent
import com.ku_stacks.ku_ring.main.calendar.type.ScheduleType
import com.ku_stacks.ku_ring.main.calendar.type.color
import kotlinx.datetime.LocalDateTime

@Composable
internal fun AcademicScheduleItem(
    event: AcademicEvent,
    modifier: Modifier = Modifier,
) {
    val scheduleType = ScheduleType.from(event.category)

    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(46.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ScheduleColorIndicator(scheduleType = scheduleType)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = event.summary,
                    style = KuringTheme.typography.body1,
                    color = KuringTheme.colors.textTitle,
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = event.period,
                    style = KuringTheme.typography.tag2,
                    color = KuringTheme.colors.textCaption1,
                    maxLines = 1,
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        HorizontalDivider(color = KuringTheme.colors.borderline, thickness = Dp.Hairline)
    }
}

@Composable
private fun ScheduleColorIndicator(
    scheduleType: ScheduleType,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(50)

    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(4.dp)
            .background(
                color = scheduleType.color(),
                shape = shape,
            ),
    )
}

@LightAndDarkPreview
@Composable
private fun AcademicScheduleItemPreview() {
    KuringTheme {
        Column(
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .padding(20.dp)
        ) {
            AcademicScheduleItem(
                event = AcademicEvent(
                    id = 1L,
                    summary = "수강바구니 1차",
                    category = ScheduleType.EVENT.name,
                    startDateTime = LocalDateTime(
                        2025, 8, 4, 9, 30
                    ),
                    endDateTime = LocalDateTime(
                        2025, 8, 5, 13, 0
                    ),
                )
            )
        }
    }
}
