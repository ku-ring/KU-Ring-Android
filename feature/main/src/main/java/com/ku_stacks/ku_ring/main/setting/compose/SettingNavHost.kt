package com.ku_stacks.ku_ring.main.setting.compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.setting.SettingViewModel
import com.ku_stacks.ku_ring.main.setting.compose.inner_screen.SettingScreen

// TODO: 향후 compose 완전 migration 시 사용
@Composable
internal fun SettingHavHost(
    navigateToEditSubscription: () -> Unit,
    startWebViewActivity: (Int) -> Unit,
    navigateToKuringInstagram: () -> Unit,
    navigateToFeedback: () -> Unit,
    navigateToOssLicenses: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: SettingViewModel = hiltViewModel(),
) {
    BackHandler(enabled = navController.currentBackStackEntry?.destination?.route != SettingDestinations.SETTING_SCREEN) {
        navController.popBackStack()
    }

    val background = KuringTheme.colors.background
    NavHost(
        navController = navController,
        startDestination = SettingDestinations.SETTING_SCREEN,
        modifier = modifier,
        // TODO: enter, exit
    ) {
        settingNavGraph(
            navController = navController,
            viewModel = viewModel,
            navigateToEditSubscription = navigateToEditSubscription,
            startWebViewActivity = startWebViewActivity,
            navigateToKuringInstagram = navigateToKuringInstagram,
            navigateToFeedback = navigateToFeedback,
            navigateToOssLicenses = navigateToOssLicenses,
            modifier = Modifier
                .background(background)
                .fillMaxSize(),
        )
    }
}

private fun NavGraphBuilder.settingNavGraph(
    navController: NavHostController,
    viewModel: SettingViewModel,
    navigateToEditSubscription: () -> Unit,
    startWebViewActivity: (Int) -> Unit,
    navigateToKuringInstagram: () -> Unit,
    navigateToFeedback: () -> Unit,
    navigateToOssLicenses: () -> Unit,
    modifier: Modifier = Modifier,
) {
    composable(SettingDestinations.SETTING_SCREEN) {
        val isExtNotificationAllowed by viewModel.isExtNotificationAllowed.collectAsState()
        SettingScreen(
            onNavigateToEditSubscription = navigateToEditSubscription,
            isExtNotificationEnabled = isExtNotificationAllowed,
            onExtNotificationEnabledToggle = viewModel::setExtNotificationAllowed,
            onNavigateToUpdateLog = { startWebViewActivity(R.string.notion_new_contents_url) },
            onNavigateToKuringTeam = { startWebViewActivity(R.string.notion_kuring_team_url) },
            onNavigateToPrivacyPolicy = { startWebViewActivity(R.string.notion_privacy_policy_url) },
            onNavigateToServiceTerms = { startWebViewActivity(R.string.notion_terms_of_service_url) },
            onNavigateToOpenSources = { navController.navigate(SettingDestinations.OPEN_SOURCE) },
            onNavigateToKuringInstagram = navigateToKuringInstagram,
            onNavigateToFeedback = navigateToFeedback,
            modifier = modifier,
        )
    }
}