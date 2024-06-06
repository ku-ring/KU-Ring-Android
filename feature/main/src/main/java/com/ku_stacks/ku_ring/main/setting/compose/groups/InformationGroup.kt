package com.ku_stacks.ku_ring.main.setting.compose.groups

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.setting.compose.components.ChevronIcon
import com.ku_stacks.ku_ring.main.setting.compose.components.SettingGroup
import com.ku_stacks.ku_ring.main.setting.compose.components.SettingItem

@Composable
internal fun InformationGroup(
    appVersion: String,
    onNavigateToUpdateLog: () -> Unit,
    onNavigateToKuringTeam: () -> Unit,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToServiceTerms: () -> Unit,
    onNavigateToOpenSources: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingGroup(
        groupTitle = stringResource(id = R.string.setting_information_title),
        modifier = modifier,
    ) {
        SettingItem(
            iconId = R.drawable.ic_rocket_v2,
            title = stringResource(id = R.string.setting_information_app_version),
            content = { AppVersionText(appVersion = appVersion) },
        )
        SettingItem(
            iconId = R.drawable.ic_star_v2,
            title = stringResource(id = R.string.setting_information_new),
            onClick = onNavigateToUpdateLog,
            content = { ChevronIcon() },
        )
        SettingItem(
            iconId = R.drawable.ic_kuring_team_v2,
            title = stringResource(id = R.string.setting_information_kuring_team),
            onClick = onNavigateToKuringTeam,
            content = { ChevronIcon() },
        )
        SettingItem(
            iconId = R.drawable.ic_shield_v2,
            title = stringResource(id = R.string.setting_information_privacy_policy),
            onClick = onNavigateToPrivacyPolicy,
            content = { ChevronIcon() },
        )
        SettingItem(
            iconId = R.drawable.ic_check_circle_v2,
            title = stringResource(id = R.string.setting_information_service_terms),
            onClick = onNavigateToServiceTerms,
            content = { ChevronIcon() },
        )
        SettingItem(
            iconId = R.drawable.ic_cloud_web_v2,
            title = stringResource(id = R.string.setting_information_open_sources),
            onClick = onNavigateToOpenSources,
            content = { ChevronIcon() },
        )
    }
}

@Composable
private fun AppVersionText(
    appVersion: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = appVersion,
        style = TextStyle(
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(500),
            color = KuringTheme.colors.textTitle,
            letterSpacing = 0.15.sp,
        ),
        modifier = modifier,
    )
}

@LightAndDarkPreview
@Composable
private fun InformationGroupPreview() {
    KuringTheme {
        InformationGroup(
            appVersion = "2.0.1",
            onNavigateToUpdateLog = {},
            onNavigateToKuringTeam = {},
            onNavigateToPrivacyPolicy = {},
            onNavigateToServiceTerms = {},
            onNavigateToOpenSources = {},
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxWidth(),
        )
    }
}