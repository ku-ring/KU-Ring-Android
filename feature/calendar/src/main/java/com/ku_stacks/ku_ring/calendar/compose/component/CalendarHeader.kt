package com.ku_stacks.ku_ring.calendar.compose.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.calendar.compose.component.NavigateDirection.NEXT
import com.ku_stacks.ku_ring.calendar.compose.component.NavigateDirection.PREVIOUS
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
            .padding(horizontal = 35.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = KuringTheme.typography.body2SB,
            color = KuringTheme.colors.textTitle,
        )
        Spacer(modifier = Modifier.weight(1f))

        // TODO: 좌측 방향 chevron 아이콘 추가 및 적용
        NavigateIcon(
            imageVector = Icons.Default.ChevronLeft,
            onClick = { onChevronClick(PREVIOUS.value) },
        )

        Spacer(modifier = Modifier.width(16.dp))

        // TODO: 우측 방향 chevron 아이콘 추가 및 적용
        NavigateIcon(
            imageVector = Icons.Default.ChevronRight,
            onClick = { onChevronClick(NEXT.value) },
        )
    }
}

@Composable
private fun NavigateIcon(
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = imageVector,
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
