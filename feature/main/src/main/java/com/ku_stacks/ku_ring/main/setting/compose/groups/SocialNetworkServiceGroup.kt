package com.ku_stacks.ku_ring.main.setting.compose.groups

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringThemeTest
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.setting.compose.components.ChevronIcon
import com.ku_stacks.ku_ring.main.setting.compose.components.SettingGroup
import com.ku_stacks.ku_ring.main.setting.compose.components.SettingItem

@Composable
internal fun SocialNetworkServiceGroup(
    onNavigateToKuringInstagram: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingGroup(
        groupTitle = stringResource(id = R.string.setting_sns_title),
        modifier = modifier,
    ) {
        SettingItem(
            iconId = R.drawable.ic_instagram_v2,
            title = stringResource(id = R.string.setting_sns_instagram),
            onClick = onNavigateToKuringInstagram,
            content = { ChevronIcon() },
        )
    }
}

@LightAndDarkPreview
@Composable
private fun SocialNetworkServiceGroupPreview() {
    KuringThemeTest {
        SocialNetworkServiceGroup(
            onNavigateToKuringInstagram = {},
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxWidth(),
        )
    }
}