package com.ku_stacks.ku_ring.main.search.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
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
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.search.SearchViewModel
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    modifier: Modifier = Modifier,
    searchState: SearchState = rememberSearchState(),
    tabPages: List<SearchTabInfo> = SearchTabInfo.values().toList()
) {

    SearchScreen(
        modifier = modifier,
        onNavigationClick = { viewModel.onCloseNavigationClick() },
        searchState = searchState,
        tabPages = tabPages,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SearchScreen(
    searchState: SearchState,
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier,
    tabPages: List<SearchTabInfo>,
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
            query = searchState.query.text,
            onQueryUpdate = { searchState.query = TextFieldValue(it) },
            placeholderText = stringResource(id = R.string.search_enter_keyword),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 0.dp)
        )

        Text(
            text = stringResource(id = R.string.search_result),
            style = TextStyle(
                color = CaptionGray1,
                fontSize = 16.sp,
                fontFamily = Pretendard,
                lineHeight = 27.sp,
            ),
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 18.dp, start = 20.dp, bottom = 10.dp)
        )

        val pagerState = rememberPagerState(pageCount = { tabPages.size })
        val coroutineScope = rememberCoroutineScope()

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = BoxBackgroundColor2,
            indicator = {},
            divider = {},
            modifier = Modifier
                .padding(horizontal = 20.dp)
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

        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) {index ->
            when (tabPages[index]) {
                SearchTabInfo.Notice -> {
                    Text(text = "공지")
                }
                SearchTabInfo.Professor -> {
                    Text(text = "교수명")
                }
            }
        }
    }
}

@Composable
fun rememberSearchState(
    query: String = "",
) : SearchState {
    return remember {
        SearchState(
            query = TextFieldValue(query),
            currentTab = "공지탭"
        )
    }
}

@LightAndDarkPreview
@Composable
private fun SearchScreenPreview() {
    KuringTheme {
        SearchScreen(
            searchState = rememberSearchState("산학협력"),
            onNavigationClick = {},
            modifier = Modifier.fillMaxSize(),
            tabPages = SearchTabInfo.values().toList(),
        )
    }
}
