package com.ku_stacks.ku_ring.main.notice.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.ku_stacks.ku_ring.designsystem.components.DoubleTapBackHandler
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.main.notice.compose.components.KuringBotFab
import com.ku_stacks.ku_ring.main.notice.compose.components.NoticeScreenHeader
import com.ku_stacks.ku_ring.main.notice.compose.inner_screen.NoticeTabScreens
import com.ku_stacks.ku_ring.thirdparty.di.LocalNavigator

@Composable
internal fun NoticeScreen(
    onSearchIconClick: () -> Unit,
    onNotificationIconClick: () -> Unit,
    onNoticeClick: (Notice) -> Unit,
    onNavigateToEditDepartment: () -> Unit,
    onNavigateToLibrarySeat: () -> Unit,
    onBackSingleTap: () -> Unit,
    onBackDoubleTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DoubleTapBackHandler(
        enabled = true,
        tapInterval = 2000L,
        onSingleTap = onBackSingleTap,
        onDoubleTap = onBackDoubleTap,
    )

    NoticeCompositionLocalProvider {
        val navigator = LocalNavigator.current
        val context = LocalContext.current
        val kuringBotFabState = LocalKuringBotFabState.current
        Scaffold(
            topBar = {
                NoticeScreenHeader(
                    onSearchIconClick = onSearchIconClick,
                    onNotificationIconClick = onNotificationIconClick,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = kuringBotFabState.isVisible,
                    enter = fadeIn(tween(40)),
                    exit = fadeOut(targetAlpha = 1f),
                ) {
                    KuringBotFab(onClick = { navigator.navigateToKuringBot(context) })
                }
            },
            modifier = modifier,
        ) { contentPadding ->
            NoticeTabScreens(
                onNoticeClick = onNoticeClick,
                onNavigateToEditDepartment = onNavigateToEditDepartment,
                onNavigateToLibrarySeat = onNavigateToLibrarySeat,
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun NoticeScreenPreview() {
    KuringTheme {
        NoticeScreen(
            onSearchIconClick = {},
            onNotificationIconClick = {},
            onNoticeClick = {},
            onNavigateToEditDepartment = {},
            onNavigateToLibrarySeat = {},
            onBackSingleTap = {},
            onBackDoubleTap = {},
            modifier = Modifier
                .fillMaxSize()
                .background(KuringTheme.colors.background),
        )
    }
}