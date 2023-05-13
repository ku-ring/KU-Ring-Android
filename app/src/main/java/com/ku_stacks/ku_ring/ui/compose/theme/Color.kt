package com.ku_stacks.ku_ring.ui.compose.theme

import androidx.compose.material.Colors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.ku_stacks.ku_ring.R

private val KuringGreen: Color
    @Composable get() = colorResource(R.color.kus_green)
private val KuringGreen50: Color
    @Composable get() = colorResource(R.color.kus_green_50)
private val KuringSecondaryGreen: Color
    @Composable get() = colorResource(R.color.kus_secondary_green)

val colorPalette: Colors
    @Composable get() = lightColors(
        primary = KuringGreen,
        primaryVariant = KuringGreen50,
        secondary = KuringSecondaryGreen,
    )