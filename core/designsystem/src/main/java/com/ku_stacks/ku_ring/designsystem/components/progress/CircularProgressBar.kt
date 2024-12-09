package com.ku_stacks.ku_ring.designsystem.components.progress

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme

@Composable
fun CircularProgressBar(
    progress: Float,
    strokeWidth: Dp,
    modifier: Modifier = Modifier,
    backgroundColor: Color = KuringTheme.colors.mainPrimary,
    foregroundColor: Color = KuringTheme.colors.gray100,
    strokeCap: StrokeCap = StrokeCap.Square,
) {
    CircularProgressIndicator(
        progress = progress,
        color = foregroundColor,
        backgroundColor = backgroundColor,
        strokeWidth = strokeWidth,
        strokeCap = strokeCap,
        modifier = modifier,
    )
}

@LightAndDarkPreview
@Composable
private fun CircularProgressBarPreview() {
    KuringTheme{
        CircularProgressBar(
            progress = 0.5f,
            strokeWidth = 5.dp
        )
    }
}
