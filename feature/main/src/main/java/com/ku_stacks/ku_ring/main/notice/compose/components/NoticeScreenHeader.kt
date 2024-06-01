package com.ku_stacks.ku_ring.main.notice.compose.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.topbar.KuringIconTopBar
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.R

@Composable
internal fun NoticeScreenHeader(
    onSearchIconClick: () -> Unit,
    onNotificationIconClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    KuringIconTopBar(modifier = modifier) {
        NoticeScreenHeaderIcon(
            resourceId = R.drawable.ic_search_v2,
            contentDescription = stringResource(id = R.string.notice_screen_header_search),
            onClick = onSearchIconClick,
        )
        NoticeScreenHeaderIcon(
            resourceId = R.drawable.ic_bell_v2,
            contentDescription = stringResource(id = R.string.notice_screen_header_notification),
            onClick = onNotificationIconClick,
        )
    }
}

@Composable
private fun NoticeScreenHeaderIcon(
    @DrawableRes resourceId: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(id = resourceId),
            contentDescription = contentDescription,
            tint = KuringTheme.colors.gray600,
        )
    }
}

@LightAndDarkPreview
@Composable
private fun NoticeScreenHeaderPreview() {
    KuringTheme {
        NoticeScreenHeader(
            onSearchIconClick = {},
            onNotificationIconClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}