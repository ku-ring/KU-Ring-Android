package com.ku_stacks.ku_ring.auth.compose.signout.inner_screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ku_stacks.ku_ring.designsystem.components.KuringAlertDialog
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_out_button_cancel
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_out_button_confirm
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_out_dialog_text

@Composable
internal fun SignOutDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    KuringAlertDialog(
        text = stringResource(sign_out_dialog_text),
        onConfirm = onConfirm,
        onCancel = onDismiss,
        onDismiss = onDismiss,
        confirmText = stringResource(sign_out_button_confirm),
        cancelText = stringResource(sign_out_button_cancel),
        confirmTextColor = KuringTheme.colors.warning
    )
}

@LightAndDarkPreview
@Composable
private fun SignOutDialogPreview() {
    KuringTheme {
        SignOutDialog(
            onDismiss = {},
            onConfirm = {},
        )
    }
}