package com.ku_stacks.ku_ring.main.club.compose.inner_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ku_stacks.ku_ring.compose.locals.LocalNavigator
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.ClubCategory
import com.ku_stacks.ku_ring.domain.ClubDivision
import com.ku_stacks.ku_ring.domain.ClubSummary
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.club.compose.ClubListUiState
import com.ku_stacks.ku_ring.main.club.compose.ClubListViewModel
import com.ku_stacks.ku_ring.main.club.compose.components.ClubDivisionBottomSheet
import com.ku_stacks.ku_ring.main.club.compose.components.ClubDivisionChipButtonGroup
import com.ku_stacks.ku_ring.main.club.compose.components.ClubTabRow
import com.ku_stacks.ku_ring.main.club.compose.components.ClubTopBar
import com.ku_stacks.ku_ring.ui.club.ClubItemColumn
import com.ku_stacks.ku_ring.ui.club.ClubListSortButtonRow
import com.ku_stacks.ku_ring.ui.club.ClubSortOption
import com.ku_stacks.ku_ring.ui.club.ClubSummaryPreviewParameterProvider
import com.ku_stacks.ku_ring.ui.dialog.LoginAlertDialog
import kotlinx.coroutines.flow.flowOf

@Composable
fun ClubListScreen(
    onNavigateToClubDetail: (Int) -> Unit,
    onNavigateToClubSubscription: () -> Unit,
    onNavigateToNotification: () -> Unit,
    viewModel: ClubListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val clubSummaryFlow = viewModel.clubsFlow.collectAsLazyPagingItems()
    val pagerState = rememberPagerState(
        initialPage = uiState.selectedCategory.ordinal,
        pageCount = { ClubCategory.entries.size }
    )
    var isLoginDialogVisible by remember { mutableStateOf(false) }

    LifecycleResumeEffect(Unit) {
        isLoginDialogVisible = false
        onPauseOrDispose { }
    }

    LaunchedEffect(pagerState.settledPage) {
        val selectedCategory = ClubCategory.entries[pagerState.settledPage]
        viewModel.updateSelectedCategory(selectedCategory)
    }

    ClubListScreen(
        uiState = uiState,
        clubSummaries = clubSummaryFlow,
        pagerState = pagerState,
        onNavigateToClubDetail = { onNavigateToClubDetail(it.id) },
        onNavigateToClubSubscription = onNavigateToClubSubscription,
        onNavigateToNotification = onNavigateToNotification,
        onSelectedDivisionsChange = viewModel::updateSelectedDivisions,
        onSelectedDivisionReset = viewModel::resetSelectedDivisions,
        onBottomSheetVisibilityChange = viewModel::updateBottomSheetVisibility,
        onSubscriptionToggle = { clubSummary ->
            if (viewModel.isUserLoggedIn()) {
                viewModel.updateClubSubscription(clubSummary)
            } else {
                isLoginDialogVisible = true
            }
        },
        onSortOptionChange = viewModel::updateSortOption,
    )

    if (isLoginDialogVisible) {
        val navigator = LocalNavigator.current
        val context = LocalContext.current

        LoginAlertDialog(
            onConfirm = { navigator.navigateToAuth(context) },
            onDismiss = { isLoginDialogVisible = false },
        )
    }
}

@Composable
private fun ClubListScreen(
    uiState: ClubListUiState,
    clubSummaries: LazyPagingItems<ClubSummary>,
    pagerState: PagerState,
    onNavigateToClubDetail: (ClubSummary) -> Unit,
    onNavigateToClubSubscription: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onSelectedDivisionsChange: (Set<ClubDivision>) -> Unit,
    onSelectedDivisionReset: () -> Unit,
    onBottomSheetVisibilityChange: () -> Unit,
    onSubscriptionToggle: (ClubSummary) -> Unit,
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
            onResetClick = onSelectedDivisionReset,
            onExpandClick = onBottomSheetVisibilityChange,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
        ) {
            Text(
                text = stringResource(R.string.club_item_count, clubSummaries.itemCount.toString()),
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
            ClubItemColumn(
                clubSummaries = clubSummaries,
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
    @PreviewParameter(ClubSummaryPreviewParameterProvider ::class) clubSummaries: List<ClubSummary>,
) {
    KuringTheme {
        val pagingData = PagingData.from(
            data = clubSummaries,
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
            clubSummaries = clubFlow,
            pagerState = pagerState,
            onNavigateToClubDetail = {},
            onNavigateToClubSubscription = {},
            onNavigateToNotification = {},
            onSelectedDivisionsChange = {},
            onSelectedDivisionReset = {},
            onBottomSheetVisibilityChange = {},
            onSubscriptionToggle = {},
            onSortOptionChange = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
