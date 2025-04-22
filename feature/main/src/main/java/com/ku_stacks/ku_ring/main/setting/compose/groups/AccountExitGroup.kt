package com.ku_stacks.ku_ring.main.setting.compose.groups

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.R.drawable.ic_trashcan_v2
import com.ku_stacks.ku_ring.main.R.drawable.ic_user_x_v2
import com.ku_stacks.ku_ring.main.R.string.setting_logout
import com.ku_stacks.ku_ring.main.R.string.setting_sign_out
import com.ku_stacks.ku_ring.main.setting.UserProfileState
import com.ku_stacks.ku_ring.main.setting.compose.components.ChevronIcon
import com.ku_stacks.ku_ring.main.setting.compose.components.SettingItem

@Composable
internal fun AccountExitGroup(
    userProfileState: UserProfileState,
    onLogoutClick: () -> Unit,
    onNavigateToSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (userProfileState is UserProfileState.LoggedIn) {
        Column(modifier.background(KuringTheme.colors.background)) {
            SettingItem(
                iconId = ic_user_x_v2,
                title = stringResource(setting_logout),
                onClick = onLogoutClick,
                content = { ChevronIcon() },
            )

            SettingItem(
                iconId = ic_trashcan_v2,
                title = stringResource(setting_sign_out),
                onClick = onNavigateToSignOut,
                content = { ChevronIcon() },
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun AccountExitGroupPreview() {
    KuringTheme {
        Column {
            AccountExitGroup(
                userProfileState = UserProfileState.LoggedIn("쿠링이"),
                onLogoutClick = {},
                onNavigateToSignOut = {}
            )
        }
    }
}
