package com.ku_stacks.ku_ring.main

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.archive.compose.ArchiveScreen
import com.ku_stacks.ku_ring.main.campusmap.CampusMapScreen
import com.ku_stacks.ku_ring.main.notice.compose.NoticeScreen
import com.ku_stacks.ku_ring.main.setting.SettingViewModel
import com.ku_stacks.ku_ring.main.setting.compose.OpenSourceActivity
import com.ku_stacks.ku_ring.main.setting.compose.inner_screen.SettingScreen
import com.ku_stacks.ku_ring.thirdparty.compose.KuringCompositionLocalProvider
import com.ku_stacks.ku_ring.thirdparty.di.LocalNavigator
import com.ku_stacks.ku_ring.ui_util.KuringNavigator
import com.ku_stacks.ku_ring.ui_util.showToast
import com.ku_stacks.ku_ring.util.findActivity

@Composable
fun MainScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.let { MainScreenRoute.of(it) }
        ?: MainScreenRoute.Notice

    KuringCompositionLocalProvider {
        val navigator = LocalNavigator.current
        val activity =
            LocalContext.current.findActivity() ?: return@KuringCompositionLocalProvider

        Scaffold(
            bottomBar = {
                MainScreenNavigationBar(
                    currentRoute = currentRoute,
                    onNavigationItemClick = { navController.navigate(it) },
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            modifier = modifier,
        ) {
            NavHost(
                navController = navController,
                startDestination = MainScreenRoute.Notice,
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                enterTransition = {
                    val initialRoute = MainScreenRoute.of(initialState)
                    val targetRoute = MainScreenRoute.of(targetState)
                    val enterDirection =
                        slideDirection(
                            initialRoute = initialRoute,
                            targetRoute = targetRoute,
                        )
                    slideIntoContainer(enterDirection)
                },
                exitTransition = {
                    val initialRoute = MainScreenRoute.of(initialState)
                    val targetRoute = MainScreenRoute.of(targetState)
                    val enterDirection =
                        slideDirection(
                            initialRoute = initialRoute,
                            targetRoute = targetRoute,
                        )
                    slideOutOfContainer(enterDirection)
                },
            ) {
                mainScreenNavGraph(
                    navigator = navigator,
                    activity = activity,
                )
            }
        }
    }
}

private fun MainScreenRoute.screenOrder() =
    when (this) {
        is MainScreenRoute.Notice -> 0
        is MainScreenRoute.Archive -> 1
        is MainScreenRoute.CampusMap -> 2
        is MainScreenRoute.Settings -> 3
    }

private fun slideDirection(
    initialRoute: MainScreenRoute,
    targetRoute: MainScreenRoute,
) = if (initialRoute.screenOrder() > targetRoute.screenOrder()) {
    AnimatedContentTransitionScope.SlideDirection.Right
} else {
    AnimatedContentTransitionScope.SlideDirection.Left
}

fun NavGraphBuilder.mainScreenNavGraph(
    navigator: KuringNavigator,
    activity: Activity,
) {
    composable<MainScreenRoute.Notice> {
        NoticeScreen(
            onSearchIconClick = {
                navigator.navigateToSearch(activity)
            },
            onNotificationIconClick = {
                navigator.navigateToEditSubscription(activity)
            },
            onNoticeClick = {
                navigator.navigateToNoticeWeb(activity, it)
            },
            onNavigateToEditDepartment = {
                navigator.navigateToEditSubscribedDepartment(activity)
            },
            onNavigateToLibrarySeat = {
                navigator.navigateToLibrarySeat(activity)
            },
            onBackSingleTap = {
                activity.showToast(R.string.home_finish_if_back_again)
            },
            onBackDoubleTap = {
                activity.finish()
            },
            modifier =
            Modifier
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
        )
    }
    composable<MainScreenRoute.Archive> {
        ArchiveScreen(
            modifier =
            Modifier
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
        )
    }
    composable<MainScreenRoute.CampusMap> {
        CampusMapScreen()
    }
    composable<MainScreenRoute.Settings> {
        // TODO by mwy3055: SettingScreen 내부도 navigation으로 migrate해야 함
        val viewModel = hiltViewModel<SettingViewModel>()
        val isExtNotificationAllowed by viewModel.isExtNotificationAllowed.collectAsStateWithLifecycle()
        SettingScreen(
            onNavigateToEditSubscription = { navigator.navigateToEditSubscription(activity) },
            isExtNotificationEnabled = isExtNotificationAllowed,
            onExtNotificationEnabledToggle = viewModel::setExtNotificationAllowed,
            onNavigateToUpdateLog = {
                activity.startWebView(navigator, R.string.notion_new_contents_url)
            },
            onNavigateToKuringTeam = {
                activity.startWebView(navigator, R.string.notion_kuring_team_url)
            },
            onNavigateToPrivacyPolicy = {
                activity.startWebView(navigator, R.string.notion_privacy_policy_url)
            },
            onNavigateToServiceTerms = {
                activity.startWebView(navigator, R.string.notion_terms_of_service_url)
            },
            onNavigateToOpenSources = { OpenSourceActivity.start(activity) },
            onNavigateToKuringInstagram = { activity.navigateToKuringInstagram() },
            onNavigateToFeedback = { navigator.navigateToFeedback(activity) },
            modifier =
            Modifier
                .background(KuringTheme.colors.background)
                .fillMaxWidth()
                .wrapContentHeight(),
        )
    }
}

private fun Activity.startWebView(
    navigator: KuringNavigator,
    @StringRes urlId: Int,
) {
    val url = getString(urlId)
    navigator.navigateToNotionView(this, url)
    this.overridePendingTransition(
        R.anim.anim_slide_right_enter,
        R.anim.anim_stay_exit,
    )
}

private fun Activity.navigateToKuringInstagram() {
    val intent = getInstagramIntent()
    startActivity(intent)
}

private fun Activity.getInstagramIntent(): Intent {
    val packageName = getString(R.string.instagram_package)
    val appScheme = getString(R.string.instagram_app_scheme)
    val webScheme = getString(R.string.instagram_web_scheme)
    return try {
        packageManager.getPackageInfo(packageName, 0)
        Intent(Intent.ACTION_VIEW, Uri.parse(appScheme))
    } catch (e: PackageManager.NameNotFoundException) {
        Intent(Intent.ACTION_VIEW, Uri.parse(webScheme))
    }
}
