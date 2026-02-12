package com.ku_stacks.ku_ring.main.club.compose.inner_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.Club
import com.ku_stacks.ku_ring.domain.ClubCategory
import com.ku_stacks.ku_ring.domain.ClubDivision
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.club.compose.ClubListUiState
import com.ku_stacks.ku_ring.main.club.compose.ClubListViewModel
import com.ku_stacks.ku_ring.main.club.compose.components.ClubDivisionBottomSheet
import com.ku_stacks.ku_ring.main.club.compose.components.ClubDivisionChipButtonGroup
import com.ku_stacks.ku_ring.main.club.compose.components.ClubTabRow
import com.ku_stacks.ku_ring.main.club.compose.components.ClubTopBar
import com.ku_stacks.ku_ring.ui.club.ClubListSortButtonRow
import com.ku_stacks.ku_ring.ui.club.ClubSortOption
import com.ku_stacks.ku_ring.ui.club.ClubsPreviewParameterProvider
import com.ku_stacks.ku_ring.ui.club.LazyPagingClubItemColumn
import kotlinx.coroutines.flow.flowOf

@Composable
fun ClubListScreen(
    onNavigateToClubDetail: (Int) -> Unit,
    onNavigateToClubSubscription: () -> Unit,
    onNavigateToNotification: () -> Unit,
    viewModel: ClubListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val clubs = viewModel.clubsFlow.collectAsLazyPagingItems()
    val pagerState = rememberPagerState(
        initialPage = uiState.selectedCategory.ordinal,
        pageCount = { ClubCategory.entries.size }
    )

    LaunchedEffect(pagerState.settledPage) {
        val selectedCategory = ClubCategory.entries[pagerState.settledPage]
        viewModel.updateSelectedCategory(selectedCategory)
    }

    ClubListScreen(
        uiState = uiState,
        clubs = clubs,
        pagerState = pagerState,
        onNavigateToClubDetail = { onNavigateToClubDetail(it.id) },
        onNavigateToClubSubscription = onNavigateToClubSubscription,
        onNavigateToNotification = onNavigateToNotification,
        onSelectedDivisionsChange = viewModel::updateSelectedDivisions,
        onBottomSheetVisibilityChange = viewModel::updateBottomSheetVisibility,
        onSubscriptionToggle = viewModel::updateClubSubscription,
        onSortOptionChange = viewModel::updateSortOption
    )
}

@Composable
private fun ClubListScreen(
    uiState: ClubListUiState,
    clubs: LazyPagingItems<Club>,
    pagerState: PagerState,
    onNavigateToClubDetail: (Club) -> Unit,
    onNavigateToClubSubscription: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onSelectedDivisionsChange: (Set<ClubDivision>) -> Unit,
    onBottomSheetVisibilityChange: () -> Unit,
    onSubscriptionToggle: (Club) -> Unit,
    onSortOptionChange: (ClubSortOption) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp)
) {
    if (uiState.isDivisionBottomSheetVisible) {
        ClubDivisionBottomSheet(
            selectedItems = uiState.selectedDivisions,
            onConfirm = { newValue ->
                onSelectedDivisionsChange(newValue)
                onBottomSheetVisibilityChange()
            },
            onDismissRequest = onBottomSheetVisibilityChange,
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KuringTheme.colors.background),
    ) {
        ClubTopBar(
            onNavigateToSubscription = onNavigateToClubSubscription,
            onNavigateToNotification = onNavigateToNotification,
        )

        ClubTabRow(
            pagerState = pagerState,
        )

        Spacer(modifier = Modifier.height(16.dp))

        ClubDivisionChipButtonGroup(
            selectedDivisions = uiState.selectedDivisions,
            onChipClick = { division ->
                val selectedDivision = uiState.selectedDivisions
                if (selectedDivision.contains(division)) {
                    onSelectedDivisionsChange(selectedDivision - division)
                } else {
                    onSelectedDivisionsChange(selectedDivision + division)
                }
            },
            onResetClick = { onSelectedDivisionsChange(setOf()) },
            onExpandClick = onBottomSheetVisibilityChange,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
        ) {
            Text(
                text = stringResource(R.string.club_item_count, clubs.itemCount.toString()),
                style = KuringTheme.typography.body1,
                color = KuringTheme.colors.textCaption1,
            )

            ClubListSortButtonRow(
                selectedOption = uiState.sortOption,
                onOptionSelect = onSortOptionChange
            )
        }

        HorizontalPager(
            state = pagerState,
        ) {
            LazyPagingClubItemColumn(
                clubs = clubs,
                onClubSubscribeToggle = onSubscriptionToggle,
                onClubItemClick = onNavigateToClubDetail,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun ClubListScreenPreview(
    @PreviewParameter(ClubsPreviewParameterProvider::class) clubs: List<Club>,
) {
    KuringTheme {
        val pagingData = PagingData.from(
            data = clubs,
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(false),
                prepend = LoadState.NotLoading(false),
                append = LoadState.NotLoading(false)
            )
        )
        val clubFlow = flowOf(pagingData).collectAsLazyPagingItems()
        val pagerState = rememberPagerState(pageCount = { ClubCategory.entries.size })
        val uiState = ClubListUiState(
            isDivisionBottomSheetVisible = false,
            selectedDivisions = setOf(ClubDivision.ENGINEERING),
            sortOption = ClubSortOption.END_OF_RECRUITMENT,
            selectedCategory = ClubCategory.ACADEMIC
        )

        ClubListScreen(
            uiState = uiState,
            clubs = clubFlow,
            pagerState = pagerState,
            onNavigateToClubDetail = {},
            onNavigateToClubSubscription = {},
            onNavigateToNotification = {},
            onSelectedDivisionsChange = {},
            onBottomSheetVisibilityChange = {},
            onSubscriptionToggle = {},
            onSortOptionChange = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
