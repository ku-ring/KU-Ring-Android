package com.ku_stacks.ku_ring.main.archive.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ku_stacks.ku_ring.designsystem.components.KuringAlertDialog
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.R

@Composable
internal fun DeleteArchivedNoticesAlertDialog(
    isDeletePopupVisible: Boolean,
    isDeleteAllNotices: Boolean,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var textId by rememberSaveable { mutableIntStateOf(R.string.alert_dialog_delete_notices) }
    LaunchedEffect(isDeletePopupVisible, isDeleteAllNotices) {
        if (isDeletePopupVisible) {
            textId =
                if (isDeleteAllNotices) R.string.alert_dialog_delete_all_notices else R.string.alert_dialog_delete_notices
        }
    }

    AnimatedVisibility(
        visible = isDeletePopupVisible,
        modifier = modifier,
    ) {
        KuringAlertDialog(
            text = stringResource(id = textId),
            onConfirm = onDelete,
            confirmText = stringResource(id = R.string.alert_dialog_delete_text),
            onCancel = onDismiss,
            cancelText = stringResource(id = R.string.alert_dialog_cancel_text),
            confirmTextColor = KuringTheme.colors.warning,
        )
    }
}

@LightAndDarkPreview
@Composable
private fun DeleteSelectedNoticesAlertDialogPreview() {
    KuringTheme {
        DeleteArchivedNoticesAlertDialog(
            isDeletePopupVisible = true,
            isDeleteAllNotices = true,
            onDelete = { },
            onDismiss = { },
        )
    }
}