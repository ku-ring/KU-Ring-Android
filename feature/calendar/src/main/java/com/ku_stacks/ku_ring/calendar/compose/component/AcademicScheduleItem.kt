package com.ku_stacks.ku_ring.calendar.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.calendar.type.ScheduleType
import com.ku_stacks.ku_ring.calendar.type.color
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme

@Composable
internal fun AcademicScheduleItem(
    title: String,
    period: String,
    scheduleType: ScheduleType,
    modifier: Modifier = Modifier,
) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
        ) {
            ScheduleColorIndicator(scheduleType = scheduleType)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title,
                    style = KuringTheme.typography.body1,
                    color = KuringTheme.colors.textTitle,
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = period,
                    style = KuringTheme.typography.tag2,
                    color = KuringTheme.colors.textCaption1,
                    maxLines = 1
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
                title = "수강바구니 1차",
                period = "8. 04 (월) 오전 9:30 - 8. 05 (화) 오후 5:00",
                scheduleType = ScheduleType.DEGREE,
            )
        }
    }
}
