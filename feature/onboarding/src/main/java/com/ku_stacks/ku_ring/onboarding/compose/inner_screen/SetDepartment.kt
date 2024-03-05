package com.ku_stacks.ku_ring.onboarding.compose.inner_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme

@Composable
internal fun SetDepartment(
    onSetDepartmentComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onSetDepartmentComplete,
        modifier = modifier,
    ) {
        Text(text = "전공학과를 설정해 주세요")
    }
}

@LightAndDarkPreview
@Composable
private fun SetDepartmentPreview() {
    KuringTheme {
        SetDepartment(
            onSetDepartmentComplete = { },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

