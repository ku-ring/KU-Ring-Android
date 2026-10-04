package com.ku_stacks.ku_ring.main.notice.compose.inner_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.main.notice.NoticeScreenTabItem
import com.ku_stacks.ku_ring.main.notice.compose.components.NoticeScreenTabRow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NoticeTabScreens(
    onNoticeClick: (Notice) -> Unit,
    onTopTabSelect: (String) -> Unit,
    onNavigateToEditDepartment: () -> Unit,
    onNavigateToAcademicEvent: () -> Unit,
    onNavigateToLibrarySeat: () -> Unit,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState { NoticeScreenTabItem.entries.size },
) {
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .distinctUntilChanged()
            .drop(1)
            .collect { page ->
                onTopTabSelect(NoticeScreenTabItem.entries[page].koreanName)
            }
    }

    Column(modifier = modifier) {
        NoticeScreenTabRow(pagerState = pagerState)
        NoticeHorizontalPager(
            pagerState = pagerState,
            onNoticeClick = onNoticeClick,
            onNavigateToEditDepartment = onNavigateToEditDepartment,
            onNavigateToAcademicEvent = onNavigateToAcademicEvent,
            onNavigateToLibrarySeat = onNavigateToLibrarySeat,
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@LightAndDarkPreview
@Composable
private fun NoticeTabsPreview() {
    KuringTheme {
        NoticeTabScreens(
            onNoticeClick = {},
            onTopTabSelect = {},
            onNavigateToEditDepartment = {},
            onNavigateToAcademicEvent = {},
            onNavigateToLibrarySeat = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
