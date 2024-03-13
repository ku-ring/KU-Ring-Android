package com.ku_stacks.ku_ring.main.notice.compose.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.main.R

@Composable
internal fun NoticeScreenHeader(
    onStorageIconClick: () -> Unit,
    onSearchIconClick: () -> Unit,
    onNotificationIconClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(vertical = 12.dp)
            .padding(start = 20.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_kuring),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.weight(1f))
        NoticeScreenHeaderIcons(
            onStorageIconClick = onStorageIconClick,
            onSearchIconClick = onSearchIconClick,
            onNotificationIconClick = onNotificationIconClick,
        )
    }
}

@Composable
private fun NoticeScreenHeaderIcons(
    onStorageIconClick: () -> Unit,
    onSearchIconClick: () -> Unit,
    onNotificationIconClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        // TODO: 보관함 아이콘 지우고 bottom navigation으로 옮기기
        NoticeScreenHeaderIcon(
            resourceId = R.drawable.ic_storage,
            contentDescription = stringResource(id = R.string.notice_screen_header_storage),
            onClick = onStorageIconClick,
        )
        NoticeScreenHeaderIcon(
            resourceId = R.drawable.ic_search,
            contentDescription = stringResource(id = R.string.notice_screen_header_search),
            onClick = onSearchIconClick,
        )
        NoticeScreenHeaderIcon(
            resourceId = R.drawable.ic_bell,
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
        )
    }
}

@LightAndDarkPreview
@Composable
private fun NoticeScreenHeaderPreview() {
    KuringTheme {
        NoticeScreenHeader(
            onStorageIconClick = {},
            onSearchIconClick = {},
            onNotificationIconClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}