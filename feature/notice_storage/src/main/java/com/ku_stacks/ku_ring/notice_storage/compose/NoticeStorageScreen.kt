package com.ku_stacks.ku_ring.notice_storage.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.notice_storage.NoticeStorageViewModel
import com.ku_stacks.ku_ring.notice_storage.R
import com.ku_stacks.ku_ring.notice_storage.compose.components.DeleteSelectedNoticesAlertDialog
import com.ku_stacks.ku_ring.notice_storage.compose.components.NoticeStorageTopBar
import com.ku_stacks.ku_ring.notice_storage.compose.components.StoredNotices
import com.ku_stacks.ku_ring.ui_util.preview_data.previewNotices

@Composable
fun NoticeStorageScreen(
    onNoticeClick: (Notice) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NoticeStorageViewModel = hiltViewModel(),
) {
    val notices by viewModel.savedNotices.collectAsState()
    val selectedNoticeIds by viewModel.selectedNoticeIds.collectAsState()
    val isAllNoticesSelected by viewModel.isAllNoticesSelected.collectAsState()
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
        selectedNoticeIds = selectedNoticeIds,
        toggleNoticeSelection = viewModel::toggleNoticeSelection,
        onShowDeleteAlertDialog = {
            if (selectedNoticeIds.isNotEmpty()) {
                isDeleteDialogVisible = true
            }
        },
        isDeletePopupVisible = isDeleteDialogVisible,
        isDeleteAllNotices = isAllNoticesSelected,
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