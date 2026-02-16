package com.ku_stacks.ku_ring.club.subscription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ku_stacks.ku_ring.club.R.string.club_subscription_item_count
import com.ku_stacks.ku_ring.club.subscription.component.ClubSubscriptionTopBar
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.Club
import com.ku_stacks.ku_ring.ui.club.ClubItemColumn
import com.ku_stacks.ku_ring.ui.club.ClubListSortButtonRow
import com.ku_stacks.ku_ring.ui.club.ClubSortOption
import com.ku_stacks.ku_ring.ui.club.ClubsPreviewParameterProvider
import kotlinx.coroutines.flow.flowOf

@Composable
fun ClubSubscriptionScreen(
    onNavigateUp: () -> Unit,
    onNavigateToClubDetail: (Int) -> Unit,
    viewModel: ClubSubscriptionViewModel = hiltViewModel(),
) {
    val sortOption by viewModel.sortOption.collectAsStateWithLifecycle()
    val clubs = viewModel.subscribedClubsFlow.collectAsLazyPagingItems()

    ClubSubscriptionScreen(
        sortOption = sortOption,
        clubs = clubs,
        onNavigateUp = onNavigateUp,
        onSortOptionChange = viewModel::updateSortOption,
        onSubscriptionToggle = viewModel::updateClubSubscription,
        onNavigateToClubDetail = { onNavigateToClubDetail(it.id) }
    )
}

@Composable
private fun ClubSubscriptionScreen(
    sortOption: ClubSortOption,
    clubs: LazyPagingItems<Club>,
    onNavigateUp: () -> Unit,
    onSortOptionChange: (ClubSortOption) -> Unit,
    onSubscriptionToggle: (Club) -> Unit,
    onNavigateToClubDetail: (Club) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp)
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KuringTheme.colors.background),
    ) {
        ClubSubscriptionTopBar(onNavigateUp = onNavigateUp)

        HorizontalDivider(
            thickness = Dp.Hairline,
            color = KuringTheme.colors.borderline,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
        ) {
            Text(
                text = stringResource(club_subscription_item_count, clubs.itemCount.toString()),
                style = KuringTheme.typography.body1,
                color = KuringTheme.colors.textCaption1,
            )

            ClubListSortButtonRow(
                selectedOption = sortOption,
                onOptionSelect = onSortOptionChange
            )
        }

        ClubItemColumn(
            clubs = clubs,
            onClubSubscribeToggle = onSubscriptionToggle,
            onClubItemClick = onNavigateToClubDetail,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun ClubSubscriptionScreenPreview(
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
    val clubFlow = flowOf(pagingData).collectAsLazyPagingItems()

    KuringTheme {
        ClubSubscriptionScreen(
            sortOption = ClubSortOption.END_OF_RECRUITMENT,
            clubs = clubFlow,
            onNavigateUp = {},
            onSortOptionChange = {},
            onSubscriptionToggle = {},
            onNavigateToClubDetail = {},
        )
    }
}
