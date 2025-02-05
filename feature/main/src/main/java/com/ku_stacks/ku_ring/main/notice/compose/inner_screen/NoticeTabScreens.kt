package com.ku_stacks.ku_ring.main.notice.compose.inner_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.main.notice.NoticeScreenTabItem
import com.ku_stacks.ku_ring.main.notice.compose.components.NoticeScreenTabRow

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NoticeTabScreens(
    onNoticeClick: (Notice) -> Unit,
    onNavigateToEditDepartment: () -> Unit,
    onNavigateToLibrarySeat: () -> Unit,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState { NoticeScreenTabItem.values().size },
) {
    Column(modifier = modifier) {
        NoticeScreenTabRow(pagerState = pagerState)
        NoticeHorizontalPager(
            pagerState = pagerState,
            onNoticeClick = onNoticeClick,
            onNavigateToEditDepartment = onNavigateToEditDepartment,
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
            onNavigateToEditDepartment = {},
            onNavigateToLibrarySeat = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}