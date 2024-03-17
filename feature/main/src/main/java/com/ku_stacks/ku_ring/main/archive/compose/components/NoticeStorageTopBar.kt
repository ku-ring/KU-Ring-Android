package com.ku_stacks.ku_ring.main.archive.compose.components

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
import com.ku_stacks.ku_ring.main.R

@Composable
internal fun NoticeStorageTopBar(
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
        title = stringResource(id = R.string.archive_screen_title),
        navigation = if (isSelectModeEnabled) {
            { TopBarNavigation() }
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
internal fun TopBarNavigation(
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
private fun NoticeStorageTopBarPreview() {
    var isSelectedModeEnabled by remember { mutableStateOf(false) }
    KuringTheme {
        NoticeStorageTopBar(
            isSelectModeEnabled = isSelectedModeEnabled,
            onSelectModeEnabled = { isSelectedModeEnabled = true },
            onSelectModeDisabled = { isSelectedModeEnabled = false },
            onSelectAllNotices = { },
        )
    }
}