package com.ku_stacks.ku_ring.calendar.compose.component.calendar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.calendar.compose.component.calendar.NavigateDirection.NEXT
import com.ku_stacks.ku_ring.calendar.compose.component.calendar.NavigateDirection.PREVIOUS
import com.ku_stacks.ku_ring.designsystem.R.drawable.ic_chevron_date_left
import com.ku_stacks.ku_ring.designsystem.R.drawable.ic_chevron_date_right
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme

@Composable
internal fun CalendarHeader(
    text: String,
    onChevronClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(top = 8.dp, bottom = 8.dp, start = 34.dp, end = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = KuringTheme.typography.viewTitle,
            color = KuringTheme.colors.textTitle,
        )
        Spacer(modifier = Modifier.weight(1f))

        // TODO: 좌측 방향 chevron 아이콘 추가 및 적용
        NavigateIcon(
            iconRes = ic_chevron_date_left,
            onClick = { onChevronClick(PREVIOUS.value) },
        )

        Spacer(modifier = Modifier.width(24.dp))

        // TODO: 우측 방향 chevron 아이콘 추가 및 적용
        NavigateIcon(
            iconRes = ic_chevron_date_right,
            onClick = { onChevronClick(NEXT.value) },
        )
    }
}

@Composable
private fun NavigateIcon(
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = ImageVector.vectorResource(iconRes),
        contentDescription = null,
        tint = KuringTheme.colors.mainPrimary,
        modifier = modifier.clickable(onClick = onClick)
    )
}

private enum class NavigateDirection(val value: Int) {
    PREVIOUS(-1), NEXT(1)
}

@LightAndDarkPreview
@Composable
private fun CalendarHeaderPreview() {
    KuringTheme {
        CalendarHeader(
            text = "8월 2025",
            onChevronClick = {},
        )
    }
}
