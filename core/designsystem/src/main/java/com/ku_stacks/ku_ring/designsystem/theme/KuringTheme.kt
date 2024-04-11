package com.ku_stacks.ku_ring.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

object KuringTheme {
    val colors: KuringColors
        @Composable get() = LocalKuringColors.current
}

/**
 * 쿠링 테마이다. UI에서 쿠링 테마를 적용할 떄 호출하는 함수이다.
 *
 * @param isDarkMode 다크 모드 적용 여부
 */
@Composable
fun KuringTheme(
    isDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorPalette = if (isDarkMode) kuringDarkColors() else kuringLightColors()
    ApplyKuringTheme(
        colors = colorPalette,
        content = content,
    )
}

/**
 * 쿠링 테마이다. 현재 색깔 테마가 선언되어 있다.
 *
 * @param colors 쿠링 색깔 테마
 */
@Composable
internal fun ApplyKuringTheme(
    colors: KuringColors = KuringTheme.colors,
    content: @Composable () -> Unit,
) {
    val rememberedKuringColors = remember {
        colors.copy()
    }.apply { updateColorsFrom(colors) }
    CompositionLocalProvider(LocalKuringColors provides rememberedKuringColors) {
        // TODO: MaterialTheme.kt 보고 ProvideTextStyle() 추가하기
        content()
    }
}