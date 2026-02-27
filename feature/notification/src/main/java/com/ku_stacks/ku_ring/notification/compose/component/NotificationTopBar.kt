package com.ku_stacks.ku_ring.notification.compose.component

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.designsystem.components.topbar.CenterTitleTopBar
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.feature.notification.R.string.notification_cd_back
import com.ku_stacks.ku_ring.feature.notification.R.string.notification_cd_edit_subscription
import com.ku_stacks.ku_ring.feature.notification.R.string.notification_top_bar_title

@Composable
fun NotificationTopBar(
    onNavigationClick: () -> Unit,
    onEditSubscriptionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CenterTitleTopBar(
        title = stringResource(notification_top_bar_title),
        navigation = {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_v2),
                contentDescription = stringResource(notification_cd_back),
                tint = KuringTheme.colors.textBody,
            )
        },
        action = {
            Icon(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = stringResource(notification_cd_edit_subscription),
                tint = KuringTheme.colors.textBody,
            )
        },
        onNavigationClick = onNavigationClick,
        onActionClick = onEditSubscriptionClick,
        modifier = modifier,
    )
}
