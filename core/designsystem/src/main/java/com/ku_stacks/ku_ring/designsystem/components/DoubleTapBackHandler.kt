package com.ku_stacks.ku_ring.designsystem.components

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun DoubleTapBackHandler(
    enabled: Boolean,
    tapInterval: Long,
    onSingleTap: () -> Unit,
    onDoubleTap: () -> Unit,
) {
    var lastPressedTime by remember { mutableLongStateOf(0L) }

    BackHandler(enabled = enabled) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastPressedTime < tapInterval) {
            onDoubleTap()
        } else {
            lastPressedTime = currentTime
            onSingleTap()
        }
    }
}