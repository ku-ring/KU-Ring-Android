package com.ku_stacks.ku_ring.ui.club

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.ku_stacks.ku_ring.designsystem.R.drawable.ic_alert_circle_v2
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.indicator.PagingLoadingIndicator
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.ClubSummary
import com.ku_stacks.ku_ring.ui.R.string.club_list_no_item
import kotlinx.coroutines.flow.flowOf

@Composable
fun ClubItemColumn(
    clubSummaries: LazyPagingItems<ClubSummary>,
    onClubSubscribeToggle: (ClubSummary) -> Unit,
    onClubItemClick: (ClubSummary) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp),
        modifier = modifier,
    ) {
        when (clubSummaries.loadState.refresh) {
            is LoadState.Loading -> {
                item {
                    PagingLoadingIndicator(
                        modifier = modifier
                            .padding(top = 50.dp)
                            .fillMaxWidth()
                    )
                }
            }

            is LoadState.Error -> {
                item {
                    EmptyClubItemView()
                }
            }

            is LoadState.NotLoading -> {
                if (clubSummaries.itemCount == 0) {
                    item {
                        EmptyClubItemView()
                    }
                } else {
                    items(
                        count = clubSummaries.itemCount,
                        key = clubSummaries.itemKey(),
                        contentType = clubSummaries.itemContentType { it.javaClass }
                    ) { index ->
                        clubSummaries[index]?.let { club ->
                            ClubItemCard(
                                clubSummary = club,
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
        }
    }
}

@Composable
private fun EmptyClubItemView(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize(),
    ) {
        Spacer(modifier = Modifier.height(120.dp))

        Image(
            imageVector = ImageVector.vectorResource(ic_alert_circle_v2),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(club_list_no_item),
            style = KuringTheme.typography.body1,
            color = KuringTheme.colors.textCaption1,
        )
    }
}

@LightAndDarkPreview
@Composable
private fun ClubItemColumnPreview(
    @PreviewParameter(ClubSummaryPreviewParameterProvider::class) clubs: List<ClubSummary>,
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

    KuringTheme {
        Box(
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .height(500.dp)
                .padding(20.dp),
        ) {
            ClubItemColumn(
                clubSummaries = clubs,
                onClubSubscribeToggle = {},
                onClubItemClick = {},
                modifier = Modifier
            )
        }
    }
}
