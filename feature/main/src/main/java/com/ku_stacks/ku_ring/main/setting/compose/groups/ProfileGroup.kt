package com.ku_stacks.ku_ring.main.setting.compose.groups

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.R.drawable.ic_user_v2
import com.ku_stacks.ku_ring.main.R.string.setting_profile_sign_in
import com.ku_stacks.ku_ring.main.setting.compose.components.ChevronIcon
import com.ku_stacks.ku_ring.main.setting.compose.components.SettingItem

@Composable
internal fun ProfileGroup(
    nickName: String?,
    onNavigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title: String = nickName ?: stringResource(setting_profile_sign_in)
    val onClick = onNavigateToSignIn.takeIf { nickName.isNullOrBlank() }
    val content: @Composable () -> Unit = { ChevronIcon().takeIf { nickName.isNullOrBlank() } }

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
            nickName = "쿠링이",
            onNavigateToSignIn = {},
        )
    }
}

@LightAndDarkPreview
@Composable
private fun NotLoggedInProfilePreview() {
    KuringTheme {
        ProfileGroup(
            nickName = null,
            onNavigateToSignIn = {},
        )
    }
}