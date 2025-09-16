package com.ku_stacks.ku_ring.main.calendar.compose.component.calendar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.R.drawable.ic_chevron_date_left
import com.ku_stacks.ku_ring.designsystem.R.drawable.ic_chevron_date_right
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.R.string.calendar_btn_navigate_next
import com.ku_stacks.ku_ring.main.R.string.calendar_btn_navigate_previous

@Composable
internal fun CalendarHeader(
    text: String,
    onChevronClick: (NavigateDirection) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(contentPadding)
            .padding(top = 8.dp, bottom = 8.dp, start = 14.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = KuringTheme.typography.viewTitle,
            color = KuringTheme.colors.textTitle,
        )
        Spacer(modifier = Modifier.weight(1f))

        NavigateIcon(
            iconRes = ic_chevron_date_left,
            contentDescription = calendar_btn_navigate_previous,
            onClick = { onChevronClick(NavigateDirection.PREVIOUS) },
        )

        Spacer(modifier = Modifier.width(24.dp))

        NavigateIcon(
            iconRes = ic_chevron_date_right,
            contentDescription = calendar_btn_navigate_next,
            onClick = { onChevronClick(NavigateDirection.NEXT) },
        )
    }
}

@Composable
private fun NavigateIcon(
    @DrawableRes iconRes: Int,
    @StringRes contentDescription: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = ImageVector.vectorResource(iconRes),
        contentDescription = stringResource(id = contentDescription),
        tint = KuringTheme.colors.mainPrimary,
        modifier = modifier.clickable(onClick = onClick)
    )
}

internal enum class NavigateDirection(val value: Int) {
    PREVIOUS(-1), NEXT(1)
}

@LightAndDarkPreview
@Composable
private fun CalendarHeaderPreview() {
    KuringTheme {
        CalendarHeader(
            text = "8월 2025",
            onChevronClick = {},
            contentPadding = PaddingValues(horizontal = 20.dp),
        )
    }
}
