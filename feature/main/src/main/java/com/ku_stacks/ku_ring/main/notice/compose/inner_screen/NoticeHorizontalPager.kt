package com.ku_stacks.ku_ring.main.notice.compose.inner_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.main.notice.NoticeScreenTabItem

@Composable
internal fun NoticeHorizontalPager(
    pagerState: PagerState,
    onNoticeClick: (Notice) -> Unit,
    onNavigateToEditDepartment: () -> Unit,
    onNavigateToLibrarySeat: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier,
    ) { pageIndex ->
        when(pageIndex) {
            0 -> {
                DepartmentNoticeScreen(
                    viewModel = hiltViewModel(),
                    onNoticeClick = onNoticeClick,
                    onNavigateToEditDepartment = onNavigateToEditDepartment,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            3 -> {
                LibraryNoticeScreen(
                    shortCategoryName = NoticeScreenTabItem.values()[pageIndex].shortCategoryName,
                    onNavigateToLibrarySeat = onNavigateToLibrarySeat,
                    onNoticeClick = onNoticeClick,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            else -> {
                CategoryNoticeScreen(
                    shortCategoryName = NoticeScreenTabItem.values()[pageIndex].shortCategoryName,
                    onNoticeClick = onNoticeClick,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}