package com.ku_stacks.ku_ring.notice_storage.compose

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.CenterTitleTopBar
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.NoticeItem
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.theme.Pretendard
import com.ku_stacks.ku_ring.designsystem.theme.TextBody
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.notice_storage.R
import com.ku_stacks.ku_ring.ui_util.preview_data.previewNotices

@Composable
private fun NoticeStorageScreen(
    isSelectModeEnabled: Boolean,
    onSelectModeEnabled: () -> Unit,
    onSelectModeDisabled: () -> Unit,
    onSelectAllNotices: () -> Unit,
    notices: List<Notice>,
    onNoticeClick: (Notice) -> Unit,
    selectedNoticeIds: Set<String>,
    toggleNoticeSelection: (String) -> Unit,
    onDeleteNotices: () -> Unit,
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
        StoredNotices(
            notices = notices,
            onNoticeClick = onNoticeClick,
            isSelectModeEnabled = isSelectModeEnabled,
            selectedNoticeIds = selectedNoticeIds,
            toggleNoticeSelection = toggleNoticeSelection,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )
        if (isSelectModeEnabled) {
            KuringCallToAction(
                text = stringResource(id = R.string.cta_text),
                onClick = onDeleteNotices,
                enabled = selectedNoticeIds.isNotEmpty(),
                blur = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }
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

@Composable
private fun StoredNotices(
    notices: List<Notice>,
    onNoticeClick: (Notice) -> Unit,
    selectedNoticeIds: Set<String>,
    isSelectModeEnabled: Boolean,
    toggleNoticeSelection: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(
            items = notices,
            key = { it.articleId },
        ) { notice ->
            SelectedNotice(
                notice = notice,
                isSelectModeEnabled = isSelectModeEnabled,
                toggleNoticeSelection = toggleNoticeSelection,
                onNoticeClick = onNoticeClick,
                selectedNoticeIds = selectedNoticeIds,
            )
        }
    }
}

@Composable
private fun SelectedNotice(
    notice: Notice,
    isSelectModeEnabled: Boolean,
    toggleNoticeSelection: (String) -> Unit,
    onNoticeClick: (Notice) -> Unit,
    selectedNoticeIds: Set<String>,
    modifier: Modifier = Modifier,
) {
    NoticeItem(
        notice = notice,
        onClick = {
            if (isSelectModeEnabled) {
                toggleNoticeSelection(notice.articleId)
            } else {
                onNoticeClick(notice)
            }
        },
        modifier = modifier,
    ) {
        if (isSelectModeEnabled) {
            NoticeSelectionStateImage(
                isSelected = notice.articleId in selectedNoticeIds,
                modifier = Modifier.fillMaxHeight(),
            )
        }
    }
}

@Composable
private fun NoticeSelectionStateImage(
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = isSelected,
        label = "notice selection icon",
    ) {
        val imageId = if (it) R.drawable.ic_check_checked else R.drawable.ic_check_unchecked
        Image(
            painter = painterResource(id = imageId),
            contentDescription = null,
            modifier = modifier,
        )
    }
}

@LightAndDarkPreview
@Composable
private fun NoticeStorageScreenPreview() {
    var isSelectModeEnabled by remember { mutableStateOf(false) }
    var selectedNoticeIds by remember { mutableStateOf(emptySet<String>()) }
    KuringTheme {
        NoticeStorageScreen(
            isSelectModeEnabled = isSelectModeEnabled,
            onSelectModeEnabled = {
                isSelectModeEnabled = true
            },
            onSelectModeDisabled = {
                isSelectModeEnabled = false
                selectedNoticeIds = emptySet()
            },
            onSelectAllNotices = {},
            notices = previewNotices,
            onNoticeClick = {},
            selectedNoticeIds = selectedNoticeIds,
            toggleNoticeSelection = { noticeId ->
                selectedNoticeIds = if (noticeId in selectedNoticeIds) {
                    selectedNoticeIds.minus(noticeId)
                } else {
                    selectedNoticeIds.plus(noticeId)
                }
            },
            onDeleteNotices = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}