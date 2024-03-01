package com.ku_stacks.ku_ring.notice_storage.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.CenterTitleTopBar
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.theme.Pretendard
import com.ku_stacks.ku_ring.designsystem.theme.TextBody
import com.ku_stacks.ku_ring.notice_storage.R

@Composable
private fun NoticeStorageScreen(
    isSelectModeEnabled: Boolean,
    onSelectModeEnabled: () -> Unit,
    onSelectModeDisabled: () -> Unit,
    onSelectAllNotices: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colors.surface)
            .fillMaxSize(),
    ) {
        NoticeStorageTopBar(
            isSelectModeEnabled = isSelectModeEnabled,
            onSelectModeEnabled = onSelectModeEnabled,
            onSelectModeDisabled = onSelectModeDisabled,
            onSelectAllNotices = onSelectAllNotices,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun NoticeStorageTopBar(
    isSelectModeEnabled: Boolean,
    onSelectModeEnabled: () -> Unit,
    onSelectModeDisabled: () -> Unit,
    onSelectAllNotices: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val actionId =
        if (isSelectModeEnabled) R.string.top_app_bar_action_select_all else R.string.top_app_bar_action_edit
    val onActionClick = if (isSelectModeEnabled) onSelectAllNotices else onSelectModeEnabled

    CenterTitleTopBar(
        title = "",
        navigation = if (isSelectModeEnabled) {
            {
                TopBarNavigation()
            }
        } else {
            null
        },
        onNavigationClick = onSelectModeDisabled,
        action = stringResource(id = actionId),
        onActionClick = onActionClick,
        modifier = modifier,
    )
}

@Composable
private fun TopBarNavigation(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id = R.string.top_app_bar_navigation),
        style = TextStyle(
            fontSize = 18.sp,
            lineHeight = 27.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(500),
            color = TextBody,
        ),
        modifier = modifier,
    )
}


@LightAndDarkPreview
@Composable
private fun NoticeStorageScreenPreview() {
    var isSelectModeEnabled by remember { mutableStateOf(false) }
    KuringTheme {
        NoticeStorageScreen(
            isSelectModeEnabled = isSelectModeEnabled,
            onSelectModeEnabled = {
                isSelectModeEnabled = true
            },
            onSelectModeDisabled = {
                isSelectModeEnabled = false
            },
            onSelectAllNotices = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}