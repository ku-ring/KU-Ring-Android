package com.ku_stacks.ku_ring.kuringbot.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ku_stacks.ku_ring.designsystem.components.KuringAlertDialog
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.kuringbot.R

@Composable
internal fun SendQuestionDialog(
    onSend: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    KuringAlertDialog(
        text = stringResource(id = R.string.kuringbot_send_dialog_text),
        confirmText = stringResource(id = R.string.kuringbot_send_dialog_send),
        onConfirm = onSend,
        cancelText = stringResource(id = R.string.kuringbot_send_dialog_cancel),
        onCancel = onCancel,
        modifier = modifier,
    )
}

@LightAndDarkPreview
@Composable
private fun SendQuestionDialogPreview() {
    KuringTheme {
        SendQuestionDialog(
            onSend = {},
            onCancel = {},
        )
    }
}