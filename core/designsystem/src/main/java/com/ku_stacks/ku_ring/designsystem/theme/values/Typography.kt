package com.ku_stacks.ku_ring.designsystem.theme.values

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.R

val SfProDisplay = FontFamily(
    Font(R.font.sfpro_display_medium, FontWeight.Medium),
    Font(R.font.sfpro_display_regular, FontWeight.Normal),
    Font(R.font.sfpro_display_bold, FontWeight.Bold),
)

val Pretendard = FontFamily(
    Font(R.font.pretendard_black, FontWeight.Black),
    Font(R.font.pretendard_bold, FontWeight.Bold),
    Font(R.font.pretendard_extrabold, FontWeight.ExtraBold),
    Font(R.font.pretendard_extralight, FontWeight.ExtraLight),
    Font(R.font.pretendard_light, FontWeight.Light),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),
    Font(R.font.pretendard_thin, FontWeight.Thin),
)

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