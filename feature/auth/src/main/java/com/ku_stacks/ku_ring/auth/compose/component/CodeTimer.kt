package com.ku_stacks.ku_ring.auth.compose.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.util.KuringTimer

@Composable
internal fun CodeTimer(
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val timer = remember { KuringTimer(coroutineScope) }

    DisposableEffect(enabled) {
        timer.startTimer()

        onDispose {
            timer.stopTimer()
        }
    }

    Text(
        text = timer.getFormattedTime(),
        style = TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 18.sp,
            color = KuringTheme.colors.warning
        ),
        modifier = modifier
    )
}

