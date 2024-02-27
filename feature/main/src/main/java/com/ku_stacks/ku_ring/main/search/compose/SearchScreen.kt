package com.ku_stacks.ku_ring.main.search.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.CenterTitleTopBar
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.SearchTextField
import com.ku_stacks.ku_ring.designsystem.theme.BoxBackgroundColor2
import com.ku_stacks.ku_ring.designsystem.theme.CaptionGray1
import com.ku_stacks.ku_ring.designsystem.theme.KuringGreen
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.theme.Pretendard
import com.ku_stacks.ku_ring.designsystem.utils.NoRippleInteractionSource
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.domain.Staff
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.search.SearchViewModel
import com.ku_stacks.ku_ring.main.search.compose.inner_screen.NoticeSearchScreen
import com.ku_stacks.ku_ring.main.search.compose.inner_screen.StaffSearchScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier,
    searchState: SearchState = rememberSearchState(),
    tabPages: List<SearchTabInfo> = SearchTabInfo.values().toList()
) {

    SearchScreen(
        onNavigationClick = onNavigationClick,
        onClickSearch = { viewModel.onClickSearch(it) },
        searchState = searchState,
        tabPages = tabPages,
        noticeSearchResult = viewModel.noticeSearchResult,
        staffSearchResult = viewModel.staffSearchResult,
        modifier = modifier,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SearchScreen(
    onNavigationClick: () -> Unit,
    onClickSearch: (SearchState) -> Unit,
    searchState: SearchState,
    tabPages: List<SearchTabInfo>,
    noticeSearchResult: StateFlow<List<Notice>>,
    staffSearchResult: StateFlow<List<Staff>>,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.background(MaterialTheme.colors.surface)
    ) {
        CenterTitleTopBar(
            title = stringResource(id = R.string.search),
            navigation = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    tint = contentColorFor(backgroundColor = MaterialTheme.colors.surface)
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
                onSearch = { onClickSearch(searchState) }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 0.dp)
        )

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
            noticeSearchResult = noticeSearchResult,
            staffSearchResult = staffSearchResult,
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
private fun SearchResultTitle(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id = R.string.search_result),
        style = TextStyle(
            color = CaptionGray1,
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
        backgroundColor = BoxBackgroundColor2,
        indicator = {},
        divider = {},
        modifier = modifier
            .padding(horizontal = 20.dp)
            .padding(bottom = 6.dp)
            .clip(RoundedCornerShape(14.dp))
    ) {
        tabPages.forEachIndexed { index, searchTabInfo ->
            val isSelected = pagerState.currentPage == index
            val tabBackgroundColor = if (isSelected) MaterialTheme.colors.surface else Color.Transparent

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
                unselectedContentColor = CaptionGray1,
                selectedContentColor = KuringGreen,
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
    noticeSearchResult: StateFlow<List<Notice>>,
    staffSearchResult: StateFlow<List<Staff>>,
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
                    noticeSearchResult = noticeSearchResult
                )
            }
            SearchTabInfo.Staff -> {
                StaffSearchScreen(
                    staffSearchResult = staffSearchResult
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
            onClickSearch = {},
            noticeSearchResult = MutableStateFlow(emptyList()),
            staffSearchResult = MutableStateFlow(emptyList()),
            modifier = Modifier.fillMaxSize(),
            tabPages = SearchTabInfo.values().toList(),
        )
    }
}
