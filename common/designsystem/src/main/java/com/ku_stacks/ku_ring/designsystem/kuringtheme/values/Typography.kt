package com.ku_stacks.ku_ring.designsystem.kuringtheme.values

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// fontSize, fontWeight, lineHeight, letterSpacing(optional)

internal val NoticeTitle = TextStyle(
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = (20 * 1.5f).sp,
)
internal val Title1 = TextStyle(
    fontSize = 24.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = (24 * 1.42f).sp,
)
internal val Title2 = TextStyle(
    fontSize = 18.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 27.sp,
    letterSpacing = (-0.41).sp,
)
internal val ViewTitle = TextStyle(
    fontSize = 18.sp,
    fontWeight = FontWeight.SemiBold,
    lineHeight = (18 * 1.48f).sp,
    letterSpacing = (-0.41).sp,
)
internal val Body1 = TextStyle(
    fontSize = 15.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = (15 * 1.63f).sp,
)

// Body2-Regular 등은 KuringTheme.typography.Body2.copy(fontWeight = FontWeight.Regular)처럼 사용
internal val Body2 = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = (16 * 1.5f).sp,
)
internal val Caption1 = TextStyle(
    fontSize = 14.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = (14 * 1.63f).sp,
)
internal val Tag = TextStyle(
    fontSize = 12.sp,
    fontWeight = FontWeight.SemiBold,
    lineHeight = (12 * 1.63f).sp,
)
internal val TextInputField = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = (16 * 1.63f).sp,
)
internal val BottomNavigationDefault = TextStyle(
    fontSize = 10.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = (10 * 1.63f).sp,
)