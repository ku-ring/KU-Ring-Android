package com.ku_stacks.ku_ring.club.subscription.component

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.ku_stacks.ku_ring.club.R.string.club_subscription_topbar_title
import com.ku_stacks.ku_ring.designsystem.R.drawable.ic_back_v2
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.topbar.CenterTitleTopBar

@Composable
fun ClubSubscriptionTopBar(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CenterTitleTopBar(
        title = stringResource(club_subscription_topbar_title),
        navigation = {
            Icon(
                imageVector = ImageVector.vectorResource(ic_back_v2),
                contentDescription = null,
            )
        },
        onNavigationClick = onNavigateUp,
        modifier = modifier,
    )
}

@LightAndDarkPreview
@Composable
private fun ClubSubscriptionTopBarPreview() {
    ClubSubscriptionTopBar(onNavigateUp = {})
}
