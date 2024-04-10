package com.ku_stacks.ku_ring.splash.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ku_stacks.ku_ring.designsystem.components.KuringAlertDialog
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.splash.R

@Composable
fun UpdateAppDialog(
    onUpdate: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    KuringAlertDialog(
        text = stringResource(id = R.string.update_dialog_text),
        onConfirm = onUpdate,
        onCancel = onDismiss,
        modifier = modifier,
        confirmText = stringResource(id = R.string.update_dialog_confirm),
        cancelText = stringResource(id = R.string.update_dialog_dismiss),
    )
}

@LightAndDarkPreview
@Composable
private fun UpdateAppDialogPreview() {
    KuringTheme {
        UpdateAppDialog(
            onUpdate = {},
            onDismiss = {},
        )
    }
}