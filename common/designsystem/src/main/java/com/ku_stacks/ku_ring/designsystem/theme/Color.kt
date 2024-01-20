package com.ku_stacks.ku_ring.designsystem.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.ku_stacks.ku_ring.designsystem.R

private val KuringGreen: Color
    @Composable get() = colorResource(R.color.kus_green)
private val KuringGreen50: Color
    @Composable get() = colorResource(R.color.kus_green_50)
private val KuringSecondaryGreen: Color
    @Composable get() = colorResource(R.color.kus_secondary_green)
val KuringSub: Color
    @Composable get() = Color(0xFFECF9F3)
val CaptionGray1: Color
    @Composable get() = Color(0xFF868A92)
val CaptionGray2: Color
    @Composable get() = Color(0xFFAEB1B6)

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