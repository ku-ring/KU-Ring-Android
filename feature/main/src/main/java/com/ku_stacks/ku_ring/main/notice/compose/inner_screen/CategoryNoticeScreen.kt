package com.ku_stacks.ku_ring.main.notice.compose.inner_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ku_stacks.ku_ring.designsystem.components.LazyPagingNoticeItemColumn
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.main.notice.CategoryNoticeViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun CategoryNoticeScreen(
    shortCategoryName: String,
    onNoticeClick: (Notice) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CategoryNoticeViewModel = hiltViewModel(key = shortCategoryName),
) {
    LaunchedEffect(shortCategoryName) {
        viewModel.getNotices(shortCategoryName)
    }

    val noticesFlow by viewModel.noticesFlow.collectAsState()
    val notices = noticesFlow?.collectAsLazyPagingItems()

    var isRefreshing by remember { mutableStateOf(false) }
    val refreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            notices?.refresh()
            isRefreshing = false
        },
        refreshThreshold = 75.dp,
    )

    CategoryNoticeScreenContents(
        notices = notices,
        onNoticeClick = onNoticeClick,
        refreshState = refreshState,
        isRefreshing = isRefreshing,
        onNoticeShown = viewModel::insertNoticeToLocal,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CategoryNoticeScreenContents(
    notices: LazyPagingItems<Notice>?,
    onNoticeClick: (Notice) -> Unit,
    refreshState: PullRefreshState,
    isRefreshing: Boolean,
    onNoticeShown: (Notice) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        LazyPagingNoticeItemColumn(
            notices = notices,
            onNoticeClick = onNoticeClick,
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(refreshState),
            // TODO: 일단 비중요 공지만 보여주고, 나중에 정책이 결정되면 다시 수정하기
            noticeFilter = { !it.isImportant },
            // TODO: see CategoryNoticeViewModel.insertNoticeToLocal
            onNoticeShown = onNoticeShown,
        )

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}