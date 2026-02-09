package com.ku_stacks.ku_ring.designsystem.utils

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle

/**
 * 폰트에 정의된 LineHeight를 강제로 보장하는 메서드
 */
fun TextStyle.ensureLineHeight() = copy(
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None,
    ),
)