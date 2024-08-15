package com.ku_stacks.ku_ring.main.notice.compose.components

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import javax.inject.Inject

@Stable
class KuringBotFabState @Inject constructor() {
    var isVisible: Boolean by mutableStateOf(true)
        private set

    fun show() {
        isVisible = true
    }

    fun hide() {
        isVisible = false
    }
}