package com.ku_stacks.ku_ring.main.club.compose.inner_screen

import android.widget.Toast
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.ku_stacks.ku_ring.compose.locals.LocalNavigator
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.indicator.PagingLoadingIndicator
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.ClubCategory
import com.ku_stacks.ku_ring.domain.ClubDivision
import com.ku_stacks.ku_ring.domain.ClubSummary
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.club.compose.ClubListFilter
import com.ku_stacks.ku_ring.main.club.compose.ClubListSideEffect
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

@Composable
fun ClubListScreen(
    onNavigateToClubDetail: (Int) -> Unit,
    onNavigateToClubSubscription: () -> Unit,
    onNavigateToNotification: () -> Unit,
    viewModel: ClubListViewModel = hiltViewModel()
) {
    val clubFilter by viewModel.clubListFilter.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isLoginDialogVisible by remember { mutableStateOf(false) }
    var isDivisionBottomSheetVisible by remember { mutableStateOf(false) }
    val selectedCategory = clubFilter.selectedCategory

    LifecycleResumeEffect(Unit) {
        isLoginDialogVisible = false
        onPauseOrDispose { }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is ClubListSideEffect.ShowToast -> {
                        val message = context.getString(sideEffect.messageId)
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    if (selectedCategory != null) {
        val pagerState = rememberPagerState(
            initialPage = selectedCategory.ordinal,
            pageCount = { ClubCategory.entries.size }
        )

        LaunchedEffect(pagerState.settledPage) {
            val category = ClubCategory.entries[pagerState.settledPage]
            viewModel.updateSelectedCategory(category)
        }

        ClubListScreen(
            clubFilter = clubFilter,
            uiState = uiState,
            pagerState = pagerState,
            isDivisionBottomSheetVisible = isDivisionBottomSheetVisible,
            onNavigateToClubDetail = { onNavigateToClubDetail(it.id) },
            onNavigateToClubSubscription = {
                if (viewModel.isUserLoggedIn()) {
                    onNavigateToClubSubscription()
                } else {
                    isLoginDialogVisible = true
                }
            },
            onNavigateToNotification = onNavigateToNotification,
            onSelectedDivisionsChange = viewModel::updateSelectedDivisions,
            onSelectedDivisionReset = viewModel::resetSelectedDivisions,
            onBottomSheetVisibilityChange = {
                isDivisionBottomSheetVisible = !isDivisionBottomSheetVisible
            },
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
}

@Composable
private fun ClubListScreen(
    uiState: ClubListUiState,
    clubFilter: ClubListFilter,
    pagerState: PagerState,
    onNavigateToClubDetail: (ClubSummary) -> Unit,
    onNavigateToClubSubscription: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onSelectedDivisionsChange: (Set<ClubDivision>) -> Unit,
    onSelectedDivisionReset: () -> Unit,
    onBottomSheetVisibilityChange: () -> Unit,
    onSubscriptionToggle: (ClubSummary) -> Unit,
    onSortOptionChange: (ClubSortOption) -> Unit,
    isDivisionBottomSheetVisible: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp)
) {
    if (isDivisionBottomSheetVisible) {
        ClubDivisionBottomSheet(
            selectedItems = clubFilter.selectedDivisions,
            onConfirm = { newValue ->
                onSelectedDivisionsChange(newValue)
                onBottomSheetVisibilityChange()
            },
            onDismissRequest = onBottomSheetVisibilityChange,
        )
    }

    Scaffold(
        topBar = {
            ClubTopBar(
                onNavigateToSubscription = onNavigateToClubSubscription,
                onNavigateToNotification = onNavigateToNotification,
            )
        },
        containerColor = KuringTheme.colors.background,
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            ClubTabRow(
                pagerState = pagerState,
            )

            ClubDivisionChipButtonGroup(
                selectedDivisions = clubFilter.selectedDivisions,
                onChipClick = { division ->
                    val selectedDivision = clubFilter.selectedDivisions
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
                val clubSummariesSize =
                    (uiState as? ClubListUiState.Success)?.clubSummaries?.size ?: 0

                Text(
                    text = stringResource(R.string.club_item_count, clubSummariesSize.toString()),
                    style = KuringTheme.typography.body1,
                    color = KuringTheme.colors.textCaption1,
                )

                ClubListSortButtonRow(
                    selectedOption = clubFilter.sortOption,
                    onOptionSelect = onSortOptionChange
                )
            }

            HorizontalPager(
                state = pagerState,
            ) { page ->
                val pageCategory = ClubCategory.entries[page]
                when {
                    uiState is ClubListUiState.Loading || pageCategory != clubFilter.selectedCategory -> {
                        PagingLoadingIndicator(
                            modifier = Modifier
                                .padding(top = 50.dp)
                                .fillMaxSize()
                        )
                    }

                    uiState is ClubListUiState.Success -> {

                        key(clubFilter.sortOption) {
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

                    else -> {
                        Text(
                            text = stringResource(id = R.string.club_error_load_event),
                            style = KuringTheme.typography.caption1,
                            color = KuringTheme.colors.textCaption1,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 50.dp)
                                .fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@LightAndDarkPreview
@Composable
private fun ClubListScreenPreview(
    @PreviewParameter(ClubSummaryPreviewParameterProvider::class) clubSummaries: List<ClubSummary>,
) {
    KuringTheme {
        val pagerState = rememberPagerState(pageCount = { ClubCategory.entries.size })
        val clubFilter = ClubListFilter(
            selectedDivisions = setOf(ClubDivision.ENGINEERING),
            sortOption = ClubSortOption.END_OF_RECRUITMENT,
            selectedCategory = ClubCategory.ACADEMIC
        )

        ClubListScreen(
            clubFilter = clubFilter,
            uiState = ClubListUiState.Success(clubSummaries),
            pagerState = pagerState,
            onNavigateToClubDetail = {},
            onNavigateToClubSubscription = {},
            onNavigateToNotification = {},
            onSelectedDivisionsChange = {},
            onSelectedDivisionReset = {},
            onBottomSheetVisibilityChange = {},
            onSubscriptionToggle = {},
            onSortOptionChange = {},
            modifier = Modifier.fillMaxSize(),
            isDivisionBottomSheetVisible = false,
        )
    }
}
