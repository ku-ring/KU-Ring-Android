package com.ku_stacks.ku_ring.thirdparty.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.ku_stacks.ku_ring.thirdparty.di.KuringThemeEntryPoint
import com.ku_stacks.ku_ring.thirdparty.di.LocalAnalytics
import com.ku_stacks.ku_ring.thirdparty.di.LocalNavigator
import com.ku_stacks.ku_ring.thirdparty.di.LocalPreferences
import dagger.hilt.android.EntryPointAccessors

@Composable
fun KuringCompositionLocalProvider(
    content: @Composable () -> Unit
) {
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
        content()
    }
}
