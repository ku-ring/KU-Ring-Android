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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.topbar.NavigateUpTopBar
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.domain.LibraryRoom
import com.ku_stacks.ku_ring.feature.library.R
import com.ku_stacks.ku_ring.library.compose.component.ReservationButtonBottomBar
import com.ku_stacks.ku_ring.library.compose.component.SeatStatusGroup
import com.ku_stacks.ku_ring.library.compose.component.SeatStatusReloadFab

@Composable
internal fun LibrarySeatScreen(
    onBackButtonClick: () -> Unit,
    onReservationButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LibrarySeatScreen(
        onBackButtonClick = onBackButtonClick,
        onReservationButtonClick = onReservationButtonClick,
        onStatusReloadButtonClick = {},
        modifier = modifier,
        seatStatus = emptyList()
    )
}

@Composable
private fun LibrarySeatScreen(
    onBackButtonClick: () -> Unit,
    onReservationButtonClick: () -> Unit,
    onStatusReloadButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    seatStatus: List<LibraryRoom>
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
                onClick = onStatusReloadButtonClick
            )
        },
        containerColor = KuringTheme.colors.background,
        modifier = modifier
            .fillMaxWidth()
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(GRID_CELLS_NUM),
                verticalArrangement = Arrangement.spacedBy(32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                contentPadding = PaddingValues(bottom = 63.dp),
                modifier = Modifier.fillMaxSize()
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

                items(seatStatus, key = { item -> item.name }) { status ->
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

private const val GRID_CELLS_NUM = 2

@LightAndDarkPreview
@Composable
private fun LibrarySeatScreenPreview() {
    KuringTheme {
        LibrarySeatScreen(
            onBackButtonClick = {},
            onReservationButtonClick = {},
            onStatusReloadButtonClick = {},
            seatStatus = listOf(
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
                LibraryRoom(
                    name = "제 2열람실 (B구역)",
                    totalSeats = 143,
                    occupiedSeats = 15,
                    availableSeats = 128
                ),
                LibraryRoom(
                    name = "제 3열람실 (A구역)",
                    totalSeats = 143,
                    occupiedSeats = 15,
                    availableSeats = 128
                ),
                LibraryRoom(
                    name = "제 3열람실 (B구역)",
                    totalSeats = 143,
                    occupiedSeats = 15,
                    availableSeats = 128
                ),
                LibraryRoom(
                    name = "제 4열람실 (A구역)",
                    totalSeats = 143,
                    occupiedSeats = 15,
                    availableSeats = 128
                ),
            )
        )
    }
}