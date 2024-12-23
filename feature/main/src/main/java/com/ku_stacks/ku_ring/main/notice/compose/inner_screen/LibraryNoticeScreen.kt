package com.ku_stacks.ku_ring.main.notice.compose.inner_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ku_stacks.ku_ring.designsystem.components.LazyPagingNoticeItemColumn
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.notice.CategoryNoticeViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun LibraryNoticeScreen(
    shortCategoryName: String,
    onNoticeClick: (Notice) -> Unit,
    onNavigateToLibrarySeat: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CategoryNoticeViewModel = hiltViewModel(key = shortCategoryName),
) {
    LaunchedEffect(shortCategoryName) {
        viewModel.getNotices(shortCategoryName)
    }

    val noticesFlow by viewModel.noticesFlow.collectAsStateWithLifecycle()
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

    LibraryNoticeScreenContents(
        notices = notices,
        onBannerClick = onNavigateToLibrarySeat,
        onNoticeClick = onNoticeClick,
        refreshState = refreshState,
        isRefreshing = isRefreshing,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LibraryNoticeScreenContents(
    notices: LazyPagingItems<Notice>?,
    onBannerClick: () -> Unit,
    onNoticeClick: (Notice) -> Unit,
    refreshState: PullRefreshState,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Column {
            Image(
                painter = painterResource(R.drawable.kuring_library_seat_banner),
                contentDescription = null,
                modifier = Modifier
                    .background(KuringTheme.colors.background)
                    .padding(top = 12.dp).padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .clickable { onBannerClick() },
            )

            LazyPagingNoticeItemColumn(
                notices = notices,
                onNoticeClick = onNoticeClick,
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(refreshState),
                // TODO: 일단 비중요 공지만 보여주고, 나중에 정책이 결정되면 다시 수정하기
                noticeFilter = { !it.isImportant },
            )
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}