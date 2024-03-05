package com.ku_stacks.ku_ring.onboarding.compose.inner_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme

@Composable
internal fun ConfirmDepartment(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text("영상영화학과를 내 전공학과로 설정할까요?")
        Row {
            TextButton(onClick = onConfirm) {
                Text("예")
            }
            TextButton(onClick = onCancel) {
                Text("아니오")
            }
        }
    }
}

@LightAndDarkPreview
@Composable
private fun ConfirmDepartmentPreview() {
    KuringTheme {
        ConfirmDepartment(
            onConfirm = { },
            onCancel = { },
            modifier = Modifier.fillMaxSize(),
        )
    }
}