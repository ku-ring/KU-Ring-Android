package com.ku_stacks.ku_ring.main.search.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ku_stacks.ku_ring.designsystem.components.CenterTitleTopBar
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.SearchTextField
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.designsystem.utils.NoRippleInteractionSource
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.domain.Staff
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.search.SearchViewModel
import com.ku_stacks.ku_ring.main.search.compose.inner_screen.NoticeSearchScreen
import com.ku_stacks.ku_ring.main.search.compose.inner_screen.StaffSearchScreen
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNavigationClick: () -> Unit,
    onClickNotice: (Notice) -> Unit,
    modifier: Modifier = Modifier,
    searchState: SearchState = rememberSearchState(),
    tabPages: List<SearchTabInfo> = SearchTabInfo.values().toList()
) {
    val noticeList by viewModel.noticeSearchResult.collectAsStateWithLifecycle(initialValue = emptyList())
    val staffList by viewModel.staffSearchResult.collectAsStateWithLifecycle(initialValue = emptyList())
    val keywordHistories by viewModel.searchHistories.collectAsStateWithLifecycle(initialValue = emptyList())

    SearchScreen(
        onNavigationClick = onNavigationClick,
        onSearch = viewModel::onSearch,
        onClickNotice = onClickNotice,
        onClickClearSearchHistory = viewModel::clearSearchHistory,
        searchState = searchState,
        tabPages = tabPages,
        noticeList = noticeList,
        staffList = staffList,
        keywordHistories = keywordHistories,
        modifier = modifier,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SearchScreen(
    onNavigationClick: () -> Unit,
    onSearch: (SearchState) -> Unit,
    onClickNotice: (Notice) -> Unit,
    onClickClearSearchHistory: () -> Unit,
    searchState: SearchState,
    tabPages: List<SearchTabInfo>,
    noticeList: List<Notice>,
    staffList: List<Staff>,
    keywordHistories: List<String>,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.background(KuringTheme.colors.background)
    ) {
        CenterTitleTopBar(
            title = stringResource(id = R.string.search),
            navigation = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back_v2),
                    contentDescription = null,
                    tint = KuringTheme.colors.gray600,
                )
            },
            onNavigationClick = { onNavigationClick() },
            action = ""
        )

        SearchTextField(
            query = searchState.query,
            onQueryUpdate = { searchState.query = it },
            placeholderText = stringResource(id = R.string.search_enter_keyword),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    onSearch(searchState)
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 0.dp)
        )

        AnimatedVisibility(
            visible = keywordHistories.isNotEmpty(),
            exit = fadeOut() + slideOutVertically(),
        ) {
            KeywordHistorySection(
                keywordHistories = keywordHistories,
                onClickSearchHistory = {
                    searchState.query = it
                    onSearch(searchState)
                },
                onClickClearSearchHistory = {
                    onClickClearSearchHistory()
                },
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        SearchResultTitle()

        val pagerState = rememberPagerState(pageCount = { tabPages.size })

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.distinctUntilChanged()
                .collect { pageIndex ->
                    searchState.tab = SearchTabInfo.values()[pageIndex]
                }
        }

        SearchTabRow(
            pagerState = pagerState,
            tabPages = tabPages,
        )

        SearchResultHorizontalPager(
            searchState = searchState,
            pagerState = pagerState,
            tabPages = tabPages,
            noticeList = noticeList,
            staffList = staffList,
            onClickNotice = onClickNotice,
        )
    }
}

@Composable
fun rememberSearchState(
    query: String = "",
): SearchState {
    return remember {
        SearchState(
            query = query,
            tab = SearchTabInfo.Notice,
            isLoading = false,
        )
    }
}

@Composable
private fun KeywordHistorySection(
    keywordHistories: List<String>,
    onClickSearchHistory: (String) -> Unit,
    onClickClearSearchHistory: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
            Text(
                text = stringResource(id = R.string.search_keyword_history),
                style = TextStyle(
                    color = KuringTheme.colors.textCaption1,
                    fontSize = 15.sp,
                    fontFamily = Pretendard,
                    lineHeight = 24.sp,
                ),
                textAlign = TextAlign.Start,
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(id = R.string.delete_all_keyword_history),
                style = TextStyle(
                    color = KuringTheme.colors.textCaption2,
                    fontSize = 12.sp,
                    fontFamily = Pretendard,
                    lineHeight = 20.sp,
                ),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .clickable { onClickClearSearchHistory() }
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(vertical = 6.dp)
        ) {
            items(keywordHistories) {
                SearchHistoryChip(
                    text = it,
                    modifier = Modifier
                        .clickable { onClickSearchHistory(it) }
                )
            }
        }
    }
}

@Composable
private fun SearchHistoryChip(
    text: String,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(percent = 50)
    Box(
        modifier = modifier
            .background(KuringTheme.colors.mainPrimarySelected, shape)
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 22.82.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(400),
                color = KuringTheme.colors.mainPrimary,
            ),
        )
    }
}

@Composable
private fun SearchResultTitle(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id = R.string.search_result),
        style = TextStyle(
            color = KuringTheme.colors.textCaption1,
            fontSize = 16.sp,
            fontFamily = Pretendard,
            lineHeight = 27.sp,
        ),
        textAlign = TextAlign.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 18.dp, start = 20.dp, bottom = 10.dp),
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SearchTabRow(
    pagerState: PagerState,
    tabPages: List<SearchTabInfo>,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = KuringTheme.colors.gray100,
        indicator = {},
        divider = {},
        modifier = modifier
            .padding(horizontal = 20.dp)
            .padding(bottom = 6.dp)
            .clip(RoundedCornerShape(14.dp))
    ) {
        tabPages.forEachIndexed { index, searchTabInfo ->
            val isSelected = pagerState.currentPage == index
            val tabBackgroundColor =
                if (isSelected) KuringTheme.colors.background else Color.Transparent

            Tab(
                selected = isSelected,
                onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                text = {
                    Text(
                        text = stringResource(id = searchTabInfo.titleResId),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = Pretendard,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                },
                unselectedContentColor = KuringTheme.colors.textCaption1,
                selectedContentColor = KuringTheme.colors.mainPrimary,
                modifier = Modifier
                    .padding(horizontal = 6.dp, vertical = 5.dp)
                    .background(color = tabBackgroundColor, shape = RoundedCornerShape(12.dp)),
                interactionSource = NoRippleInteractionSource()
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SearchResultHorizontalPager(
    searchState: SearchState,
    pagerState: PagerState,
    tabPages: List<SearchTabInfo>,
    noticeList: List<Notice>,
    staffList: List<Staff>,
    onClickNotice: (Notice) -> Unit,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = pagerState,
        verticalAlignment = Alignment.Top,
        modifier = modifier
    ) { index ->
        when (tabPages[index]) {
            SearchTabInfo.Notice -> {
                NoticeSearchScreen(
                    searchState = searchState,
                    noticeList = noticeList,
                    onClickNotice = onClickNotice,
                )
            }

            SearchTabInfo.Staff -> {
                StaffSearchScreen(
                    searchState = searchState,
                    staffList = staffList
                )
            }
        }
    }
}

@LightAndDarkPreview
@Composable
private fun SearchScreenPreview() {
    KuringTheme {
        SearchScreen(
            searchState = rememberSearchState("산학협력"),
            onNavigationClick = {},
            onClickNotice = {},
            onSearch = {},
            onClickClearSearchHistory = {},
            noticeList = emptyList(),
            staffList = emptyList(),
            keywordHistories = emptyList(),
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
            tabPages = SearchTabInfo.values().toList(),
        )
    }
}
