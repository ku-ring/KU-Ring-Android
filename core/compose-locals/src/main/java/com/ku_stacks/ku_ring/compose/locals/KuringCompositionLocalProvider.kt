package com.ku_stacks.ku_ring.compose.locals

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.ku_stacks.ku_ring.compose.locals.di.KuringCompositionLocalEntryPoint
import com.ku_stacks.ku_ring.compose.locals.di.LocalAnalytics
import com.ku_stacks.ku_ring.compose.locals.di.LocalNavigator
import com.ku_stacks.ku_ring.compose.locals.di.LocalPreferences
import dagger.hilt.android.EntryPointAccessors

// Re-export for convenience
@Suppress("TopLevelPropertyNaming")
val LocalNavigator = com.ku_stacks.ku_ring.compose.locals.di.LocalNavigator
@Suppress("TopLevelPropertyNaming")
val LocalPreferences = com.ku_stacks.ku_ring.compose.locals.di.LocalPreferences
@Suppress("TopLevelPropertyNaming")
val LocalAnalytics = com.ku_stacks.ku_ring.compose.locals.di.LocalAnalytics

@Composable
fun KuringCompositionLocalProvider(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val entryPoint = remember {
        EntryPointAccessors.fromApplication<KuringCompositionLocalEntryPoint>(context)
    }
    val navigator = remember(entryPoint) { entryPoint.navigator() }
    val preference = remember(entryPoint) { entryPoint.preference() }
    val analytics = remember(entryPoint) { entryPoint.analytics() }
    CompositionLocalProvider(
        LocalNavigator provides navigator,
        LocalPreferences provides preference,
        LocalAnalytics provides analytics
    ) {
        content()
    }
}
