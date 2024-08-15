package com.ku_stacks.ku_ring.main.notice.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.ku_stacks.ku_ring.main.notice.compose.components.KuringBotFabState
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@Composable
fun NoticeCompositionLocalProvider(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val kuringBotFabState = remember {
        EntryPointAccessors
            .fromApplication<NoticeScreenEntryPoint>(context)
            .kuringBotFabState()
    }
    CompositionLocalProvider(LocalKuringBotFabState provides kuringBotFabState) {
        content()
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface NoticeScreenEntryPoint {
    fun kuringBotFabState(): KuringBotFabState
}

val LocalKuringBotFabState = staticCompositionLocalOf<KuringBotFabState> {
    error("No bottom sheet state")
}