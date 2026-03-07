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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.R.drawable.ic_alert_circle_v2
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.ClubSummary
import com.ku_stacks.ku_ring.ui.R.string.club_list_no_item

@Composable
fun ClubItemColumn(
    clubSummaries: List<ClubSummary>,
    onClubSubscribeToggle: (Int) -> Unit,
    onClubItemClick: (ClubSummary) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp),
        modifier = modifier,
    ) {
        when {
            clubSummaries.isEmpty() -> {
                item {
                    EmptyClubItemView()
                }
            }

            else -> {
                items(
                    items = clubSummaries,
                    key = { it.id },
                    contentType = { it.javaClass }
                ) { clubSummary ->
                    ClubItemCard(
                        clubSummary = clubSummary,
                        onClick = { onClubItemClick(clubSummary) },
                        onSubscribeToggleClick = { onClubSubscribeToggle(clubSummary.id) },
                        modifier = Modifier.fillMaxWidth()
                    )
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
    @PreviewParameter(ClubSummaryPreviewParameterProvider::class) clubSummaries: List<ClubSummary>,
) {
    KuringTheme {
        Box(
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .height(500.dp)
                .padding(20.dp),
        ) {
            ClubItemColumn(
                clubSummaries = clubSummaries,
                onClubSubscribeToggle = {},
                onClubItemClick = {},
                modifier = Modifier
            )
        }
    }
}
