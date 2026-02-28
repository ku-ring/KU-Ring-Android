package com.ku_stacks.ku_ring.club.subscription

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.ku_stacks.ku_ring.club.R.string.club_subscription_item_count
import com.ku_stacks.ku_ring.club.R.string.club_subscription_load_error
import com.ku_stacks.ku_ring.club.subscription.component.ClubSubscriptionTopBar
import com.ku_stacks.ku_ring.club.subscription.contract.ClubSubscriptionSideEffect
import com.ku_stacks.ku_ring.club.subscription.contract.ClubSubscriptionUiState
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.indicator.PagingLoadingIndicator
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.ClubSummary
import com.ku_stacks.ku_ring.ui.club.ClubItemColumn
import com.ku_stacks.ku_ring.ui.club.ClubListSortButtonRow
import com.ku_stacks.ku_ring.ui.club.ClubSortOption
import com.ku_stacks.ku_ring.ui.club.ClubSummaryPreviewParameterProvider

@Composable
fun ClubSubscriptionScreen(
    onNavigateUp: () -> Unit,
    onNavigateToClubDetail: (Int) -> Unit,
    viewModel: ClubSubscriptionViewModel = hiltViewModel(),
) {
    val sortOption by viewModel.sortOption.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is ClubSubscriptionSideEffect.ShowToast -> {
                        val message = context.getString(sideEffect.messageId)
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    ClubSubscriptionScreen(
        sortOption = sortOption,
        uiState = uiState,
        onNavigateUp = onNavigateUp,
        onSortOptionChange = viewModel::updateSortOption,
        onSubscriptionToggle = viewModel::updateClubSubscription,
        onNavigateToClubDetail = { onNavigateToClubDetail(it.id) }
    )
}

@Composable
private fun ClubSubscriptionScreen(
    sortOption: ClubSortOption,
    uiState: ClubSubscriptionUiState,
    onNavigateUp: () -> Unit,
    onSortOptionChange: (ClubSortOption) -> Unit,
    onSubscriptionToggle: (ClubSummary) -> Unit,
    onNavigateToClubDetail: (ClubSummary) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp)
) {
    Scaffold(
        topBar = { ClubSubscriptionTopBar(onNavigateUp = onNavigateUp) },
        containerColor = KuringTheme.colors.background,
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
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
                val clubSummariesSize =
                    (uiState as? ClubSubscriptionUiState.Success)?.clubSummaries?.size ?: 0

                Text(
                    text = stringResource(
                        club_subscription_item_count,
                        clubSummariesSize.toString()
                    ),
                    style = KuringTheme.typography.body1,
                    color = KuringTheme.colors.textCaption1,
                )

                ClubListSortButtonRow(
                    selectedOption = sortOption,
                    onOptionSelect = onSortOptionChange
                )
            }

            when (uiState) {
                is ClubSubscriptionUiState.Error -> {
                    Text(
                        text = stringResource(id = club_subscription_load_error),
                        style = KuringTheme.typography.caption1,
                        color = KuringTheme.colors.textCaption1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 50.dp)
                            .fillMaxWidth()
                    )
                }

                is ClubSubscriptionUiState.Loading -> {
                    PagingLoadingIndicator(
                        modifier = Modifier
                            .padding(top = 50.dp)
                            .fillMaxWidth()
                    )
                }

                is ClubSubscriptionUiState.Success -> {
                    key(sortOption) {
                        ClubItemColumn(
                            clubSummaries = uiState.clubSummaries,
                            onClubSubscribeToggle = onSubscriptionToggle,
                            onClubItemClick = onNavigateToClubDetail,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(contentPadding),
                        )
                    }
                }
            }
        }
    }
}

@LightAndDarkPreview
@Composable
private fun ClubSubscriptionScreenPreview(
    @PreviewParameter(ClubSummaryPreviewParameterProvider::class) clubSummaries: List<ClubSummary>,
) {
    KuringTheme {
        ClubSubscriptionScreen(
            sortOption = ClubSortOption.END_OF_RECRUITMENT,
            uiState = ClubSubscriptionUiState.Success(clubSummaries),
            onNavigateUp = {},
            onSortOptionChange = {},
            onSubscriptionToggle = {},
            onNavigateToClubDetail = {},
        )
    }
}
