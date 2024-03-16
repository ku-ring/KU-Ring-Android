package com.ku_stacks.ku_ring.main.notice.compose.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.KuringScrollableTabRow
import com.ku_stacks.ku_ring.designsystem.components.KuringScrollableTabRowDefaults
import com.ku_stacks.ku_ring.designsystem.components.KuringScrollableTabRowDefaults.kuringTabIndicatorOffset
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.theme.Background
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.theme.MainPrimary
import com.ku_stacks.ku_ring.main.notice.NoticeScreenTabItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NoticeScreenTabRow(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    val currentPage = pagerState.currentPage
    KuringScrollableTabRow(
        selectedTabIndex = currentPage,
        backgroundColor = Background,
        contentColor = MainPrimary,
        indicator = { tabPositions ->
            KuringScrollableTabRowDefaults.Indicator(
                modifier = Modifier
                    .kuringTabIndicatorOffset(tabPositions[currentPage])
                    .clip(RoundedCornerShape(50)),
                height = 4.dp,
            )
        },
        edgePadding = 20.dp,
        modifier = modifier,
    ) {
        NoticeScreenTabItem.values().forEachIndexed { index, noticeScreenTabItem ->
            NoticeTab(
                tab = noticeScreenTabItem,
                isSelected = (index == currentPage),
                modifier = Modifier
                    .clickable(
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                        role = Role.Tab,
                    )
                    .padding(horizontal = 17.5.dp, vertical = 12.5.dp),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@LightAndDarkPreview
@Composable
private fun NoticeScreenTabRowPreview() {
    KuringTheme {
        NoticeScreenTabRow(
            pagerState = rememberPagerState {
                NoticeScreenTabItem.values().size

            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}