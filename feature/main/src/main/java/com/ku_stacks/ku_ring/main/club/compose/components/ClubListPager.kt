package com.ku_stacks.ku_ring.main.club.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.indicator.PagingLoadingIndicator
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.Club
import com.ku_stacks.ku_ring.domain.ClubCategory
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.club.compose.preview.ClubsPreviewParameterProvider
import com.ku_stacks.ku_ring.ui.club.ClubItemCard
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun ClubListPager(
    pagerState: PagerState,
    clubPagingItem: LazyPagingItems<Club>,
    onClubSubscribeToggle: (Club) -> Unit,
    onClubItemClick: (Club) -> Unit,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier,
    ) {
        val loadState = clubPagingItem.loadState.refresh

        when {
            loadState is LoadState.Loading -> {
                PagingLoadingIndicator(
                    modifier = Modifier
                        .padding(top = 50.dp)
                        .fillMaxWidth()
                )
            }

            loadState is LoadState.Error -> {
                ClubListEmptyIndicator()
            }

            clubPagingItem.itemCount == 0 -> {
                ClubListEmptyIndicator()
            }

            else -> {
                ClubListColumn(
                    clubPagingItem = clubPagingItem,
                    onClubSubscribeToggle = onClubSubscribeToggle,
                    onClubItemClick = onClubItemClick
                )
            }
        }
    }
}

@Composable
internal fun ClubListColumn(
    clubPagingItem: LazyPagingItems<Club>,
    onClubSubscribeToggle: (Club) -> Unit,
    onClubItemClick: (Club) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(
            count = clubPagingItem.itemCount,
            key = clubPagingItem.itemKey(),
            contentType = clubPagingItem.itemContentType { it.javaClass }
        ) { index ->
            clubPagingItem[index]?.let { club ->
                ClubItemCard(
                    club = club,
                    onClick = {
                        onClubItemClick(club)
                    },
                    onSubscribeToggleClick = {
                        onClubSubscribeToggle(club.copy(isSubscribed = it))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@Composable
private fun ClubListEmptyIndicator(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize(),
    ) {
        Spacer(modifier = Modifier.height(120.dp))

        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_alert_circle_v2),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.club_list_no_item),
            style = KuringTheme.typography.body1,
            color = KuringTheme.colors.textCaption1,
        )
    }
}

@LightAndDarkPreview
@Composable
private fun ClubListPagerPreview(
    @PreviewParameter(ClubsPreviewParameterProvider::class) clubs: List<Club>,
) {
    val pagingData = PagingData.from(
        data = clubs,
        sourceLoadStates = LoadStates(
            refresh = LoadState.NotLoading(false),
            prepend = LoadState.NotLoading(false),
            append = LoadState.NotLoading(false)
        )
    )
    val clubs = flowOf(pagingData).collectAsLazyPagingItems()
    val pagerState = rememberPagerState(pageCount = { ClubCategory.entries.size })

    KuringTheme {
        ClubListPager(
            pagerState = pagerState,
            clubPagingItem = clubs,
            onClubSubscribeToggle = {},
            onClubItemClick = {},
            modifier = Modifier
                .height(500.dp)
                .padding(20.dp),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun ClubListPagerEmptyPreview() {
    val pagingData: PagingData<Club> = PagingData.from(
        data = listOf(),
        sourceLoadStates = LoadStates(
            refresh = LoadState.NotLoading(false),
            prepend = LoadState.NotLoading(false),
            append = LoadState.NotLoading(false)
        )
    )
    val clubs = flowOf(pagingData).collectAsLazyPagingItems()
    val pagerState = rememberPagerState(pageCount = { ClubCategory.entries.size })

    KuringTheme {
        ClubListPager(
            pagerState = pagerState,
            clubPagingItem = clubs,
            onClubSubscribeToggle = {},
            onClubItemClick = {},
            modifier = Modifier
                .height(500.dp)
                .padding(20.dp),
        )
    }
}

