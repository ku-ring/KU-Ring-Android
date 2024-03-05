package com.ku_stacks.ku_ring.onboarding.compose.inner_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme

@Composable
internal fun FeatureTabs(
    onNavigateToSetDepartment: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onNavigateToSetDepartment,
        modifier = modifier,
    ) {
        Text(text = "우리 대학 공지 쿠링이 알려줄게!")
    }
}

@LightAndDarkPreview
@Composable
private fun FeatureTabsPreview() {
    KuringTheme {
        FeatureTabs(
            onNavigateToSetDepartment = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}