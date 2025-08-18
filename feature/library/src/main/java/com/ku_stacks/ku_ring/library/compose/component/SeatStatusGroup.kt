package com.ku_stacks.ku_ring.library.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.feature.library.R

@Composable
internal fun SeatStatusGroup(
    roomName: String,
    totalSeat: Int,
    occupiedSeat: Int,
    availableSeat: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(color = KuringTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = occupiedSeat / totalSeat.toFloat(),
                color = KuringTheme.colors.gray100,
                backgroundColor = KuringTheme.colors.mainPrimary,
                strokeWidth = 10.dp,
                modifier = Modifier
                    .padding(horizontal = 5.75.dp)
                    .size(108.dp),
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = availableSeat.toString(),
                    style = KuringTheme.typography.titleSB.copy(
                        color = KuringTheme.colors.mainPrimary
                    ),
                )
                Text(
                    text = stringResource(R.string.library_seats_available_ratio, occupiedSeat, totalSeat),
                    style = KuringTheme.typography.caption2.copy(
                        color = KuringTheme.colors.textCaption1
                    ),
                )
            }
        }

        Text(
            text = roomName,
            style = KuringTheme.typography.body2.copy(
                fontWeight = FontWeight.Normal,
                color = KuringTheme.colors.textBody,
            ),
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}

@LightAndDarkPreview
@Composable
private fun SeatStatusGroupPreview() {
    KuringTheme {
        SeatStatusGroup(
            roomName = "제 1열람실 (A구역)",
            totalSeat = 143,
            occupiedSeat = 15,
            availableSeat = 128,
            modifier = Modifier.size(120.dp, 144.dp)
        )
    }
}