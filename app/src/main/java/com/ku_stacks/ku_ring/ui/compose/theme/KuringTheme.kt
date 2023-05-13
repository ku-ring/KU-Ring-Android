package com.ku_stacks.ku_ring.ui.compose.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable


@Composable
fun KuringTheme(content: @Composable () -> Unit) {
    MaterialTheme(colors = colorPalette, content = content)
}