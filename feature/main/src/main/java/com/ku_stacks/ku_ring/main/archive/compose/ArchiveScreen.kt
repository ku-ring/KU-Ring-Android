package com.ku_stacks.ku_ring.main.archive.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.archive.ArchiveViewModel
import com.ku_stacks.ku_ring.main.archive.compose.components.ArchiveScreenTopBar
import com.ku_stacks.ku_ring.main.archive.compose.components.ArchivedNotices
import com.ku_stacks.ku_ring.main.archive.compose.components.DeleteArchivedNoticesAlertDialog
import com.ku_stacks.ku_ring.thirdparty.di.LocalNavigator
import com.ku_stacks.ku_ring.ui_util.preview_data.previewNotices
import com.ku_stacks.ku_ring.util.findActivity

@Composable
fun ArchiveScreen(
    modifier: Modifier = Modifier,
    viewModel: ArchiveViewModel = hiltViewModel(),
) {
    val notices by viewModel.savedNotices.collectAsStateWithLifecycle()
    val isSelectModeEnabled by viewModel.isSelectedModeEnabled.collectAsStateWithLifecycle()
    val selectedNoticeIds by viewModel.selectedNoticeIds.collectAsStateWithLifecycle()
    val isAllNoticesSelected by viewModel.isAllNoticesSelected.collectAsStateWithLifecycle()
    var isDeleteDialogVisible by rememberSaveable { mutableStateOf(false) }
    val navigator = LocalNavigator.current
    val context = LocalContext.current.findActivity()

    ArchiveScreen(
        isSelectModeEnabled = isSelectModeEnabled,
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
            context?.let { navigator.navigateToNoticeWeb(it, notice) }
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
private fun ArchiveScreen(
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
            .background(KuringTheme.colors.background)
            .fillMaxSize(),
    ) {
        ArchiveScreenTopBar(
            isSelectModeEnabled = isSelectModeEnabled,
            onSelectModeEnabled = onSelectModeEnabled,
            onSelectModeDisabled = onSelectModeDisabled,
            onSelectAllNotices = onSelectAllNotices,
            modifier = Modifier.fillMaxWidth(),
        )
        ArchivedNotices(
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

    DeleteArchivedNoticesAlertDialog(
        isDeletePopupVisible = isDeletePopupVisible,
        isDeleteAllNotices = isDeleteAllNotices,
        onDelete = onDeleteNotices,
        onDismiss = onDismissDeleteAlertDialog,
    )
}

@LightAndDarkPreview
@Composable
private fun ArchiveScreenPreview() {
    var isSelectModeEnabled by remember { mutableStateOf(false) }
    var selectedNoticeIds by remember { mutableStateOf(emptySet<String>()) }
    KuringTheme {
        ArchiveScreen(
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