package com.ku_stacks.ku_ring.main.club.compose.components

import androidx.annotation.DrawableRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.ku_stacks.ku_ring.designsystem.R.drawable.ic_bell_v2
import com.ku_stacks.ku_ring.designsystem.R.drawable.ic_star_v2
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.topbar.CenterTitleTopBar
import com.ku_stacks.ku_ring.designsystem.components.topbar.TopBarAction
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.R.string.club_title
import com.ku_stacks.ku_ring.main.R.string.club_top_bar_action_notification
import com.ku_stacks.ku_ring.main.R.string.club_top_bar_action_subscribe


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ClubTopBar(
    onNavigateToSubscription: () -> Unit,
    onNavigateToNotification: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val actions = listOf(
        TopBarAction(
            action = { ClubTopBarActionIcon(ic_star_v2) },
            onClick = onNavigateToSubscription,
            clickLabel = stringResource(club_top_bar_action_subscribe),
        ),
        TopBarAction(
            action = { ClubTopBarActionIcon(ic_bell_v2) },
            onClick = onNavigateToNotification,
            clickLabel = stringResource(club_top_bar_action_notification),
        ),
    )
    
    CenterTitleTopBar(
        title = stringResource(club_title),
        actions = actions,
        modifier = modifier,
    )
}

@Composable
private fun ClubTopBarActionIcon(
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = ImageVector.vectorResource(iconRes),
        contentDescription = null,
        modifier = modifier,
    )
}

@LightAndDarkPreview
@Composable
private fun ClubTopBarPreview() {
    KuringTheme {
        ClubTopBar(
            onNavigateToSubscription = {},
            onNavigateToNotification = {},
        )
    }
}
