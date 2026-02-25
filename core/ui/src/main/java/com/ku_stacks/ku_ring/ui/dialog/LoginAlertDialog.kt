package com.ku_stacks.ku_ring.ui.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ku_stacks.ku_ring.designsystem.components.KuringAlertDialog
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.ui.R

@Composable
fun LoginAlertDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    KuringAlertDialog(
        text = stringResource(R.string.login_dialog_body),
        onConfirm = onConfirm,
        onCancel = onDismiss,
        confirmText = stringResource(R.string.login_dialog_do_login),
        cancelText = stringResource(R.string.login_dialog_cancel),
        modifier = modifier,
    )
}

@LightAndDarkPreview
@Composable
private fun LoginAlertDialogPreview() {
    KuringTheme {
        LoginAlertDialog(
            onConfirm = {},
            onDismiss = {},
        )
    }
}
