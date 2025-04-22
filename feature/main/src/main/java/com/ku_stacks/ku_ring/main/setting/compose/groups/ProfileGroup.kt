package com.ku_stacks.ku_ring.main.setting.compose.groups

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.R.drawable.ic_user_v2
import com.ku_stacks.ku_ring.main.R.string.setting_profile_sign_in
import com.ku_stacks.ku_ring.main.setting.UserProfileState
import com.ku_stacks.ku_ring.main.setting.compose.components.ChevronIcon
import com.ku_stacks.ku_ring.main.setting.compose.components.SettingItem

@Composable
internal fun ProfileGroup(
    userProfileState: UserProfileState,
    onNavigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val loggedIn = userProfileState is UserProfileState.LoggedIn
    val title: String =
        if (loggedIn) (userProfileState as UserProfileState.LoggedIn).nickname
        else stringResource(setting_profile_sign_in)
    val onClick = onNavigateToSignIn.takeIf { !loggedIn }
    val content: @Composable () -> Unit = { ChevronIcon().takeIf { !loggedIn } }

    SettingItem(
        iconId = ic_user_v2,
        title = title,
        onClick = onClick,
        content = content,
        modifier = modifier.background(KuringTheme.colors.background)
    )
}

@LightAndDarkPreview
@Composable
private fun LoggedInProfilePreview() {
    KuringTheme {
        ProfileGroup(
            userProfileState = UserProfileState.LoggedIn("쿠링이"),
            onNavigateToSignIn = {},
        )
    }
}

@LightAndDarkPreview
@Composable
private fun NotLoggedInProfilePreview() {
    KuringTheme {
        ProfileGroup(
            userProfileState = UserProfileState.LoggedIn("쿠링이"),
            onNavigateToSignIn = {},
        )
    }
}