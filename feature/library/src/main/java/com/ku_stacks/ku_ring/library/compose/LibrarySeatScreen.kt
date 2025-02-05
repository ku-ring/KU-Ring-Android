package com.ku_stacks.ku_ring.library.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.indicator.PagingLoadingIndicator
import com.ku_stacks.ku_ring.designsystem.components.topbar.NavigateUpTopBar
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.domain.LibraryRoom
import com.ku_stacks.ku_ring.feature.library.R
import com.ku_stacks.ku_ring.library.LibrarySeatViewModel
import com.ku_stacks.ku_ring.library.SeatLoadState
import com.ku_stacks.ku_ring.library.compose.component.ReservationButtonBottomBar
import com.ku_stacks.ku_ring.library.compose.component.SeatStatusGroup
import com.ku_stacks.ku_ring.library.compose.component.SeatStatusReloadFab

@Composable
internal fun LibrarySeatScreen(
    onNavigateBack: () -> Unit,
    onLaunchLibraryIntent: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LibrarySeatViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getLibrarySeatStatus()
    }

    LibrarySeatScreen(
        onBackButtonClick = onNavigateBack,
        onReservationButtonClick = onLaunchLibraryIntent,
        onStatusReloadButtonClick = viewModel::getLibrarySeatStatus,
        isLoading = uiState.isLoading,
        modifier = modifier,
        seatStatus = uiState.loadState
    )
}

@Composable
private fun LibrarySeatScreen(
    seatStatus: SeatLoadState,
    isLoading: Boolean,
    onBackButtonClick: () -> Unit,
    onReservationButtonClick: () -> Unit,
    onStatusReloadButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            NavigateUpTopBar(
                navigationIconId = R.drawable.ic_back_v2,
                onNavigationIconClick = onBackButtonClick,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        bottomBar = {
            ReservationButtonBottomBar(
                onClick = onReservationButtonClick
            )
        },
        floatingActionButton = {
            SeatStatusReloadFab(
                onClick = onStatusReloadButtonClick,
                isLoading = isLoading
            )
        },
        containerColor = KuringTheme.colors.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(GRID_CELLS_NUM),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            contentPadding = PaddingValues(bottom = 63.dp),
            modifier = Modifier
                .padding(innerPadding)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(
                    modifier = Modifier.padding(start = 20.dp)
                ) {
                    Text(
                        stringResource(R.string.library_seats_title),
                        style = TextStyle(
                            fontSize = 24.sp,
                            lineHeight = 36.sp,
                            fontFamily = Pretendard,
                            fontWeight = FontWeight(700),
                            color = KuringTheme.colors.textTitle,
                        ),
                        modifier = Modifier.padding(top = 18.dp)
                    )

                    Text(
                        text = stringResource(R.string.library_seats_description),
                        style = TextStyle.Default.copy(
                            color = KuringTheme.colors.textCaption1,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = (15 * 1.63f).sp,
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            when (seatStatus) {
                is SeatLoadState.InitialLoading -> {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        PagingLoadingIndicator()
                    }
                }

                is SeatLoadState.Error -> {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        LoadingErrorText(
                            modifier = Modifier
                        )
                    }
                }

                is SeatLoadState.Success -> {
                    items(seatStatus.rooms, key = { item -> item.name }) { status ->
                        SeatStatusGroup(
                            roomName = status.name,
                            totalSeat = status.totalSeats,
                            occupiedSeat = status.occupiedSeats,
                            availableSeat = status.availableSeats,
                            modifier = Modifier.wrapContentSize()
                        )
                    }
                }

            }
        }
    }
}


@Composable
private fun LoadingErrorText(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.library_seats_load_fail),
            fontFamily = Pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = KuringTheme.colors.textCaption1,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 30.dp),
            textAlign = TextAlign.Center,
        )
    }
}

private const val GRID_CELLS_NUM = 2

@LightAndDarkPreview
@Composable
private fun LibrarySeatScreenPreview() {
    var isLoading by remember { mutableStateOf(false) }

    KuringTheme {
        LibrarySeatScreen(
            onBackButtonClick = {},
            onReservationButtonClick = {},
            onStatusReloadButtonClick = { isLoading = !isLoading },
            isLoading = isLoading,
            seatStatus = SeatLoadState.Success(
                listOf(
                    LibraryRoom(
                        name = "제 1열람실 (A구역)",
                        totalSeats = 143,
                        occupiedSeats = 15,
                        availableSeats = 128
                    ),
                    LibraryRoom(
                        name = "제 1열람실 (B구역)",
                        totalSeats = 143,
                        occupiedSeats = 15,
                        availableSeats = 128
                    ),
                    LibraryRoom(
                        name = "제 2열람실 (A구역)",
                        totalSeats = 143,
                        occupiedSeats = 15,
                        availableSeats = 128
                    ),
                )
            )
        )
    }
}

@LightAndDarkPreview
@Composable
private fun LibrarySeatFailPreview() {
    KuringTheme {
        LibrarySeatScreen(
            onBackButtonClick = {},
            onReservationButtonClick = {},
            onStatusReloadButtonClick = { },
            isLoading = false,
            seatStatus = SeatLoadState.Error
        )
    }
}