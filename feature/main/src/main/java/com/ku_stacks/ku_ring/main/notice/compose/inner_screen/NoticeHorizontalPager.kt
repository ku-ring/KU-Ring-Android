package com.ku_stacks.ku_ring.main.notice.compose.inner_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.main.notice.NoticeScreenTabItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NoticeHorizontalPager(
    pagerState: PagerState,
    onNoticeClick: (Notice) -> Unit,
    onNavigateToEditDepartment: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier,
    ) { pageIndex ->
        if (pageIndex == 0) {
            DepartmentNoticeScreen(
                viewModel = hiltViewModel(),
                onNoticeClick = onNoticeClick,
                onNavigateToEditDepartment = onNavigateToEditDepartment,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            CategoryNoticeScreen(
                shortCategoryName = NoticeScreenTabItem.values()[pageIndex].shortCategoryName,
                onNoticeClick = onNoticeClick,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@LightAndDarkPreview
@Composable
private fun NoticeHorizontalPagerPreview() {
    val pagerState = rememberPagerState { NoticeScreenTabItem.values().size }

    KuringTheme {
        NoticeHorizontalPager(
            pagerState = pagerState,
            onNoticeClick = {},
            onNavigateToEditDepartment = { },
        )
    }
}