package com.ku_stacks.ku_ring.designsystem.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.ku_stacks.ku_ring.designsystem.R

val KuringGreen: Color
    @Composable get() = colorResource(R.color.kus_green)
val KuringGreen50: Color
    @Composable get() = colorResource(R.color.kus_green_50)
val KuringSecondaryGreen: Color
    @Composable get() = colorResource(R.color.kus_secondary_green)
val MainPrimary: Color
    get() = Color(0xFF3DBD80)
val MainPrimarySelected: Color
    @Composable get() = Color(0xFFEBF8F2)
val KuringSub: Color
    @Composable get() = Color(0xFFECF9F3)
val TextBody: Color
    @Composable get() = Color(0xFF353C49)
val TextCaption1: Color
    @Composable get() = Color(0xFF868A92)
val CaptionGray2: Color
    @Composable get() = Color(0xFFAEB1B6)
val Gray2: Color
    @Composable get() = Color(0xFF636363)
val Gray200: Color
    @Composable get() = Color(0xFFE5E5E5)
val Gray300: Color
    @Composable get() = Color(0xFF999999)
val Gray600: Color
    @Composable get() = Color(0xFF262626)
val Background: Color
    get() = Color(0xFFFFFFFF)
val BoxBackgroundColor2: Color
    @Composable get() = Color(0xFFF2F3F5)

val lightColorPalette: Colors
    @Composable get() = lightColors(
        primary = KuringGreen,
        primaryVariant = KuringGreen50,
        secondary = KuringSecondaryGreen,
    )

val darkColorPalette: Colors
    @Composable get() = darkColors(
        primary = KuringGreen,
        primaryVariant = KuringGreen50,
        secondary = KuringSecondaryGreen,
    )
