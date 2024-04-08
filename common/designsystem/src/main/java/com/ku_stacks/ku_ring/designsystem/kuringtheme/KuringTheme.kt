package com.ku_stacks.ku_ring.designsystem.kuringtheme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.ku_stacks.ku_ring.thirdparty.di.KuringThemeEntryPoint
import com.ku_stacks.ku_ring.thirdparty.di.LocalAnalytics
import com.ku_stacks.ku_ring.thirdparty.di.LocalNavigator
import com.ku_stacks.ku_ring.thirdparty.di.LocalPreferences
import dagger.hilt.android.EntryPointAccessors

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
    val context = LocalContext.current
    val navigator = remember {
        EntryPointAccessors
            .fromApplication<KuringThemeEntryPoint>(context)
            .navigator()
    }
    val preference = remember {
        EntryPointAccessors
            .fromApplication<KuringThemeEntryPoint>(context)
            .preference()
    }
    val analytics = remember {
        EntryPointAccessors
            .fromApplication<KuringThemeEntryPoint>(context)
            .analytics()
    }
    CompositionLocalProvider(
        LocalNavigator provides navigator,
        LocalPreferences provides preference,
        LocalAnalytics provides analytics
    ) {
        ApplyKuringTheme(
            colors = colorPalette,
            content = content,
        )
    }
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