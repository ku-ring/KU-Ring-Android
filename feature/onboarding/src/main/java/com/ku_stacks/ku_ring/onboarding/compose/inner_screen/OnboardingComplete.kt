package com.ku_stacks.ku_ring.onboarding.compose.inner_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme

@Composable
internal fun OnboardingComplete(
    onStartKuring: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onStartKuring,
        modifier = modifier,
    ) {
        Text(text = "학과 설정이 완료되었어요!")
    }
}

@LightAndDarkPreview
@Composable
private fun OnboardingCompletePreview() {
    KuringTheme {
        OnboardingComplete(
            onStartKuring = { },
            modifier = Modifier.fillMaxSize(),
        )
    }
}