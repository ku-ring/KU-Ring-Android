package com.ku_stacks.ku_ring.main.setting.compose.inner_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.CenterTitleTopBar
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.setting.compose.groups.FeedbackGroup
import com.ku_stacks.ku_ring.main.setting.compose.groups.InformationGroup
import com.ku_stacks.ku_ring.main.setting.compose.groups.SocialNetworkServiceGroup
import com.ku_stacks.ku_ring.main.setting.compose.groups.SubscribeGroup
import com.ku_stacks.ku_ring.ui_util.getAppVersionName

@Composable
internal fun SettingScreen(
    onNavigateToEditSubscription: () -> Unit,
    isExtNotificationEnabled: Boolean,
    onExtNotificationEnabledToggle: (Boolean) -> Unit,
    onNavigateToUpdateLog: () -> Unit,
    onNavigateToKuringTeam: () -> Unit,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToServiceTerms: () -> Unit,
    onNavigateToOpenSources: () -> Unit,
    onNavigateToKuringInstagram: () -> Unit,
    onNavigateToFeedback: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val appVersion = LocalContext.current.getAppVersionName()

    Column(modifier = modifier) {
        CenterTitleTopBar(
            title = stringResource(id = R.string.setting_screen_top_app_bar_title),
            action = {},
            modifier = Modifier.padding(vertical = 16.dp),
        )
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            SubscribeGroup(
                onNavigateToEditSubscription = onNavigateToEditSubscription,
                isExtNotificationEnabled = isExtNotificationEnabled,
                onExtNotificationEnabledToggle = onExtNotificationEnabledToggle,
            )
            SettingScreenDivider()
            InformationGroup(
                appVersion = appVersion,
                onNavigateToUpdateLog = onNavigateToUpdateLog,
                onNavigateToKuringTeam = onNavigateToKuringTeam,
                onNavigateToPrivacyPolicy = onNavigateToPrivacyPolicy,
                onNavigateToServiceTerms = onNavigateToServiceTerms,
                onNavigateToOpenSources = onNavigateToOpenSources,
            )
            SettingScreenDivider()
            SocialNetworkServiceGroup(onNavigateToKuringInstagram = onNavigateToKuringInstagram)
            SettingScreenDivider()
            FeedbackGroup(onNavigateToFeedback = onNavigateToFeedback)

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun SettingScreenDivider(modifier: Modifier = Modifier) {
    Spacer(
        modifier = modifier
            .padding(vertical = 12.dp)
            .background(KuringTheme.colors.borderline)
            .height(1.dp)
            .fillMaxWidth(),
    )
}

@LightAndDarkPreview
@Composable
private fun SettingScreenPreview() {
    KuringThemeTest {
        SettingScreen(
            onNavigateToEditSubscription = {},
            isExtNotificationEnabled = true,
            onExtNotificationEnabledToggle = {},
            onNavigateToUpdateLog = {},
            onNavigateToKuringTeam = {},
            onNavigateToPrivacyPolicy = {},
            onNavigateToServiceTerms = {},
            onNavigateToOpenSources = {},
            onNavigateToKuringInstagram = {},
            onNavigateToFeedback = {},
            modifier = Modifier
                .fillMaxSize()
                .background(KuringTheme.colors.background),
        )
    }
}