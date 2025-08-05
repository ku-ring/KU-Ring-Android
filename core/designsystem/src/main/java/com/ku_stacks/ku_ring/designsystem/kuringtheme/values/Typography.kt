package com.ku_stacks.ku_ring.designsystem.kuringtheme.values

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
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

// fontFamily, fontSize, fontWeight, lineHeight, letterSpacing(optional)
internal object KuringTypographyTokens {
    val NoticeTitle =
        KuringDefaultTextStyle.copy(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 1.5.em,
        )
    val Title1 =
        KuringDefaultTextStyle.copy(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 1.42.em,
        )
    val Title2 =
        KuringDefaultTextStyle.copy(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 1.5.em,
            letterSpacing = (-0.41).sp,
        )
    val ViewTitle =
        KuringDefaultTextStyle.copy(
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 1.48.em,
            letterSpacing = (-0.41).sp,
        )
    val TitleSB =
        KuringDefaultTextStyle.copy(
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 1.5.em,
        )
    val Body1 =
        KuringDefaultTextStyle.copy(
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 1.63.em,
        )
    // Body2-Regular, Body2-SemiBold는  KuringTheme.typography.Body2.copy(fontWeight = FontWeight.Regular)처럼 사용
    val Body2 =
        KuringDefaultTextStyle.copy(
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 1.5.em,
        )
    val Caption1 =
        KuringDefaultTextStyle.copy(
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 1.63.em,
        )
    val Caption1_1 =
        KuringDefaultTextStyle.copy(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 1.63.em,
        )
    val Caption2 =
        KuringDefaultTextStyle.copy(
            fontSize = 13.sp,
            fontWeight = FontWeight.Light,
            lineHeight = 1.5.em,
        )
    val Tag =
        KuringDefaultTextStyle.copy(
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 1.5.em,
        )
    val Tag2 =
        KuringDefaultTextStyle.copy(
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 1.5.em,
        )
    val InputField =
        KuringDefaultTextStyle.copy(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 1.63.em,
        )
    val BottomNavigationDefault =
        KuringDefaultTextStyle.copy(
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 1.63.em,
        )
}

internal val KuringDefaultTextStyle =
    TextStyle.Default.copy(
        fontFamily = Pretendard,
    )