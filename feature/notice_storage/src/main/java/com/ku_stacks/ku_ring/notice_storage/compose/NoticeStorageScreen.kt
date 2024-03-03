package com.ku_stacks.ku_ring.notice_storage.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ku_stacks.ku_ring.designsystem.components.CenterTitleTopBar
import com.ku_stacks.ku_ring.designsystem.components.KuringAlertDialog
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.NoticeItem
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.theme.Pretendard
import com.ku_stacks.ku_ring.designsystem.theme.TextBody
import com.ku_stacks.ku_ring.designsystem.theme.Warning
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.notice_storage.NoticeStorageViewModel
import com.ku_stacks.ku_ring.notice_storage.R
import com.ku_stacks.ku_ring.ui_util.preview_data.previewNotices

@Composable
fun NoticeStorageScreen(
    onNoticeClick: (Notice) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NoticeStorageViewModel = hiltViewModel(),
) {
    val notices by viewModel.savedNotices.collectAsState()
    var isDeleteDialogVisible by rememberSaveable { mutableStateOf(false) }

    NoticeStorageScreen(
        isSelectModeEnabled = viewModel.isSelectedModeEnabled,
        onSelectModeEnabled = {
            viewModel.setSelectedMode(true)
        },
        onSelectModeDisabled = {
            viewModel.setSelectedMode(false)
        },
        onSelectAllNotices = viewModel::selectAllNotices,
        notices = notices,
        onNoticeClick = { notice ->
            viewModel.updateNoticeAsReadOnStorage(notice)
            onNoticeClick(notice)
        },
        selectedNoticeIds = viewModel.selectedNoticeIds,
        toggleNoticeSelection = viewModel::toggleNoticeSelection,
        onShowDeleteAlertDialog = {
            if (viewModel.selectedNoticeIds.isNotEmpty()) {
                isDeleteDialogVisible = true
            }
        },
        isDeletePopupVisible = isDeleteDialogVisible,
        isDeleteAllNotices = viewModel.isAllNoticesSelected,
        onDeleteNotices = {
            viewModel.deleteNotices()
            isDeleteDialogVisible = false
        },
        onDismissDeleteAlertDialog = {
            isDeleteDialogVisible = false
        },
        modifier = modifier,
    )
}

@Composable
private fun NoticeStorageScreen(
    isSelectModeEnabled: Boolean,
    onSelectModeEnabled: () -> Unit,
    onSelectModeDisabled: () -> Unit,
    onSelectAllNotices: () -> Unit,
    notices: List<Notice>,
    onNoticeClick: (Notice) -> Unit,
    selectedNoticeIds: Set<String>,
    toggleNoticeSelection: (Notice) -> Unit,
    onShowDeleteAlertDialog: () -> Unit,
    isDeletePopupVisible: Boolean,
    isDeleteAllNotices: Boolean,
    onDeleteNotices: () -> Unit,
    onDismissDeleteAlertDialog: () -> Unit,
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
                onClick = onShowDeleteAlertDialog,
                enabled = selectedNoticeIds.isNotEmpty(),
                blur = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    DeleteSelectedNoticesAlertDialog(
        isDeletePopupVisible = isDeletePopupVisible,
        isDeleteAllNotices = isDeleteAllNotices,
        onDelete = onDeleteNotices,
        onDismiss = onDismissDeleteAlertDialog,
    )
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
    toggleNoticeSelection: (Notice) -> Unit,
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
    toggleNoticeSelection: (Notice) -> Unit,
    onNoticeClick: (Notice) -> Unit,
    selectedNoticeIds: Set<String>,
    modifier: Modifier = Modifier,
) {
    NoticeItem(
        notice = notice,
        onClick = {
            if (isSelectModeEnabled) {
                toggleNoticeSelection(notice)
            } else {
                onNoticeClick(notice)
            }
        },
        modifier = modifier,
    ) {
        if (isSelectModeEnabled) {
            NoticeSelectionStateImage(
                isSelected = notice.articleId in selectedNoticeIds,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 8.dp),
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

@Composable
private fun DeleteSelectedNoticesAlertDialog(
    isDeletePopupVisible: Boolean,
    isDeleteAllNotices: Boolean,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = isDeletePopupVisible,
        modifier = modifier,
    ) {
        val textId =
            if (isDeleteAllNotices) R.string.alert_dialog_delete_all_notices else R.string.alert_dialog_delete_notices
        KuringAlertDialog(
            text = stringResource(id = textId),
            onConfirm = onDelete,
            confirmText = stringResource(id = R.string.alert_dialog_delete_text),
            onCancel = onDismiss,
            cancelText = stringResource(id = R.string.alert_dialog_cancel_text),
            confirmTextColor = Warning,
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
            toggleNoticeSelection = { notice ->
                val articleId = notice.articleId
                selectedNoticeIds = if (articleId in selectedNoticeIds) {
                    selectedNoticeIds.minus(articleId)
                } else {
                    selectedNoticeIds.plus(articleId)
                }
            },
            isDeleteAllNotices = false,
            isDeletePopupVisible = false,
            onDismissDeleteAlertDialog = {},
            onShowDeleteAlertDialog = {},
            onDeleteNotices = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}