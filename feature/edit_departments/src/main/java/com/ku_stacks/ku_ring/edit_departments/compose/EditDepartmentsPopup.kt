package com.ku_stacks.ku_ring.edit_departments.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.KuringAlertDialog
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.edit_departments.R
import com.ku_stacks.ku_ring.edit_departments.uimodel.PopupUiModel

@Composable
internal fun DepartmentPopup(
    popupUiModel: PopupUiModel,
    onConfirm: (PopupUiModel) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val titleId = when (popupUiModel) {
        is PopupUiModel.AddPopupUiModel -> R.string.add_department_popup_title
        is PopupUiModel.DeletePopupUiModel -> R.string.delete_department_popup_title
        is PopupUiModel.DeleteAllPopupUiModel -> R.string.delete_all_department_popup_title
    }
    KuringAlertDialog(
        text = stringResource(id = titleId, popupUiModel.departmentKoreanName),
        onConfirm = { onConfirm(popupUiModel) },
        onDismiss = onDismiss,
        modifier = modifier,
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
                .background(MaterialTheme.colors.onSurface)
                .padding(16.dp),
        )
    }
}