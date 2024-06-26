package com.ku_stacks.ku_ring.edit_departments.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.KuringAlertDialog
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.edit_departments.R
import com.ku_stacks.ku_ring.edit_departments.uimodel.PopupUiModel

@Composable
internal fun DepartmentPopup(
    popupUiModel: PopupUiModel,
    onConfirm: (PopupUiModel) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val confirmTextColor = if (popupUiModel is PopupUiModel.AddPopupUiModel) {
        KuringTheme.colors.mainPrimary
    } else {
        KuringTheme.colors.warning
    }

    KuringAlertDialog(
        text = stringResource(id = popupUiModel.stringResId, popupUiModel.departmentKoreanName),
        onConfirm = { onConfirm(popupUiModel) },
        onCancel = onDismiss,
        modifier = modifier,
        confirmTextColor = confirmTextColor,
        cancelText = stringResource(id = R.string.department_popup_dismiss),
        confirmText = stringResource(id = popupUiModel.confirmStringRes),
    )
}

@LightAndDarkPreview
@Composable
private fun DepartmentPopupPreview() {
    KuringTheme {
        DepartmentPopup(
            popupUiModel = PopupUiModel.AddPopupUiModel("cse", "컴퓨터공학부"),
            onConfirm = {},
            onDismiss = {},
            modifier = Modifier
                .background(KuringTheme.colors.textBody)
                .padding(16.dp),
        )
    }
}