package com.ku_stacks.ku_ring.main

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ku_stacks.ku_ring.compose.locals.KuringCompositionLocalProvider
import com.ku_stacks.ku_ring.compose.locals.LocalNavigator
import com.ku_stacks.ku_ring.compose.locals.LocalPreferences
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.calendar.compose.AcademicCalendarScreen
import com.ku_stacks.ku_ring.main.campusmap.CampusMapViewModel
import com.ku_stacks.ku_ring.main.campusmap.compose.component.bottomsheet.CampusMapDetailSheetContent
import com.ku_stacks.ku_ring.main.campusmap.compose.component.bottomsheet.CampusMapDetailSheetHost
import com.ku_stacks.ku_ring.main.campusmap.compose.CampusMapScreen
import com.ku_stacks.ku_ring.main.campusmap.compose.component.bottomsheet.CampusMapSearchResultSheetContent
import com.ku_stacks.ku_ring.main.campusmap.compose.component.bottomsheet.CampusMapSearchResultSheetHost
import com.ku_stacks.ku_ring.main.campusmap.compose.inner_screen.CampusMapSearchDestination
import com.ku_stacks.ku_ring.main.campusmap.compose.inner_screen.CampusMapSearchRoute
import com.ku_stacks.ku_ring.main.club.compose.inner_screen.ClubListScreen
import com.ku_stacks.ku_ring.main.notice.compose.NoticeScreen
import com.ku_stacks.ku_ring.main.setting.SettingViewModel
import com.ku_stacks.ku_ring.main.setting.compose.OpenSourceActivity
import com.ku_stacks.ku_ring.main.setting.compose.inner_screen.SettingScreen
import com.ku_stacks.ku_ring.navigation.KuringNavigator
import com.ku_stacks.ku_ring.navigation.MainScreenRoute
import com.ku_stacks.ku_ring.util.showToast

@Composable
fun MainScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: MainScreenRoute = MainScreenRoute.Notice,
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val isCampusMapSearch = currentBackStackEntry?.isCampusMapSearch == true
    val currentRoute = currentBackStackEntry?.mainScreenRoute()
        ?: MainScreenRoute.Notice
    var campusMapDetailSheetContent by remember {
        mutableStateOf<CampusMapDetailSheetContent?>(null)
    }
    var campusMapSearchResultSheetContent by remember {
        mutableStateOf<CampusMapSearchResultSheetContent?>(null)
    }

    KuringCompositionLocalProvider {
        val navigator = LocalNavigator.current
        val activity = LocalActivity.current ?: return@KuringCompositionLocalProvider

        Box(modifier = modifier.fillMaxSize()) {
            Scaffold(
                bottomBar = {
                    if (!isCampusMapSearch) {
                        MainScreenNavigationBar(
                            currentRoute = currentRoute,
                            onNavigationItemClick = { navController.navigate(it) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                },
                contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Start + WindowInsetsSides.End + WindowInsetsSides.Bottom),
                containerColor = KuringTheme.colors.background,
                modifier = Modifier.fillMaxSize(),
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    modifier = Modifier
                        .padding(innerPadding)
                        .consumeWindowInsets(innerPadding)
                        .fillMaxSize(),
                    enterTransition = {
                        if (initialState.isCampusMapSearch || targetState.isCampusMapSearch) {
                            EnterTransition.None
                        } else {
                            val enterDirection = slideDirection(
                                initialRoute = initialState.mainScreenRoute(),
                                targetRoute = targetState.mainScreenRoute(),
                            )
                            slideIntoContainer(enterDirection)
                        }
                    },
                    exitTransition = {
                        if (initialState.isCampusMapSearch || targetState.isCampusMapSearch) {
                            ExitTransition.None
                        } else {
                            val enterDirection = slideDirection(
                                initialRoute = initialState.mainScreenRoute(),
                                targetRoute = targetState.mainScreenRoute(),
                            )
                            slideOutOfContainer(enterDirection)
                        }
                    },
                ) {
                    mainScreenNavGraph(
                        navController = navController,
                        navigator = navigator,
                        activity = activity,
                        onCampusMapDetailSheetContentChange = {
                            campusMapDetailSheetContent = it
                        },
                        onCampusMapSearchResultSheetContentChange = {
                            campusMapSearchResultSheetContent = it
                        },
                    )
                }
            }

            if (shouldShowCampusMapSheetHost(currentRoute, isCampusMapSearch)) {
                val detailContent = campusMapDetailSheetContent
                if (detailContent != null) {
                    CampusMapDetailSheetHost(
                        content = detailContent,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    campusMapSearchResultSheetContent?.let { content ->
                        CampusMapSearchResultSheetHost(
                            content = content,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
    }
}

private val NavBackStackEntry.isCampusMapSearch: Boolean
    get() = destination.route == CampusMapSearchDestination.route

private fun NavBackStackEntry.mainScreenRoute(): MainScreenRoute =
    if (isCampusMapSearch) {
        MainScreenRoute.CampusMap
    } else {
        MainScreenRoute.of(this)
    }

internal fun shouldShowCampusMapSheetHost(
    currentRoute: MainScreenRoute,
    isCampusMapSearch: Boolean,
): Boolean = currentRoute == MainScreenRoute.CampusMap && !isCampusMapSearch

private fun MainScreenRoute.screenOrder() =
    when (this) {
        is MainScreenRoute.Notice -> 0
        is MainScreenRoute.Calendar -> 1
        is MainScreenRoute.CampusMap -> 2
        is MainScreenRoute.Club -> 3
        is MainScreenRoute.Settings -> 4
    }

private fun slideDirection(
    initialRoute: MainScreenRoute,
    targetRoute: MainScreenRoute,
) = if (initialRoute.screenOrder() > targetRoute.screenOrder()) {
    AnimatedContentTransitionScope.SlideDirection.Right
} else {
    AnimatedContentTransitionScope.SlideDirection.Left
}

internal fun NavGraphBuilder.mainScreenNavGraph(
    navController: NavHostController,
    navigator: KuringNavigator,
    activity: Activity,
    onCampusMapDetailSheetContentChange: (CampusMapDetailSheetContent?) -> Unit,
    onCampusMapSearchResultSheetContentChange: (CampusMapSearchResultSheetContent?) -> Unit,
) {
    composable<MainScreenRoute.Notice> {
        NoticeScreen(
            onSearchIconClick = {
                navigator.navigateToSearch(activity)
            },
            onArchiveIconClick = {
                navigator.navigateToArchive(activity)
            },
            onNotificationIconClick = {
                navigator.navigateToNotification(activity)
            },
            onNoticeClick = {
                navigator.navigateToNoticeWeb(activity, it)
            },
            onNavigateToEditDepartment = {
                navigator.navigateToEditSubscribedDepartment(activity)
            },
            onNavigateToAcademicEvent = {
                navController.navigate(MainScreenRoute.Calendar)
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
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
        )
    }
    composable<MainScreenRoute.Calendar> {
        AcademicCalendarScreen(
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
        )
    }
    composable<MainScreenRoute.CampusMap> {
        CampusMapScreen(
            onLibrarySeatFabClick = {
                navigator.navigateToLibrarySeat(activity)
            },
            onNavigateToSearch = {
                navController.navigate(CampusMapSearchDestination)
            },
            onDetailSheetContentChange = onCampusMapDetailSheetContentChange,
            onSearchResultSheetContentChange = onCampusMapSearchResultSheetContentChange,
        )
    }
    composable<CampusMapSearchDestination> { backStackEntry ->
        val campusMapEntry = remember(backStackEntry) {
            navController.getBackStackEntry(MainScreenRoute.CampusMap.route)
        }
        val viewModel = hiltViewModel<CampusMapViewModel>(campusMapEntry)

        CampusMapSearchRoute(
            onNavigateUp = { navController.popBackStack() },
            viewModel = viewModel,
            modifier = Modifier.fillMaxSize(),
        )
    }
    composable<MainScreenRoute.Club> {
        val preferences = LocalPreferences.current
        val clubCategory by preferences.clubCategoryFlow.collectAsStateWithLifecycle()
        LaunchedEffect(Unit) {
            if (clubCategory.isBlank()) {
                navigator.navigateToClubOnboarding(activity)
            }
        }
        ClubListScreen(
            onNavigateToClubDetail = { clubId ->
                navigator.navigateToClubDetail(activity, clubId)
            },
            onNavigateToClubSubscription = {
                navigator.navigateToClubSubscription(activity)
            },
            onNavigateToNotification = {
                navigator.navigateToNotification(activity)
            }
        )
    }
    composable<MainScreenRoute.Settings> {
        // TODO by mwy3055: SettingScreen 내부도 navigation으로 migrate해야 함
        val viewModel = hiltViewModel<SettingViewModel>()
        val settingsUiState by viewModel.settingUiState.collectAsStateWithLifecycle()

        LifecycleResumeEffect(Unit) {
            viewModel.getUserData()
            onPauseOrDispose {
                // No resources to clean up
            }
        }

        SettingScreen(
            settingUiState = settingsUiState,
            onNavigateToSignIn = { navigator.navigateToAuth(activity) },
            onNavigateToEditSubscription = { navigator.navigateToEditSubscription(activity) },
            onExtNotificationEnabledToggle = viewModel::setExtNotificationAllowed,
            onAcademicEventNotificationEnabledToggle = { value ->
                viewModel.setAcademicEventNotificationAllowed(
                    value = value,
                    onFail = { activity.showToast(R.string.setting_subscribe_academic_events_fail) },
                )
            },
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
            onLogoutClick = viewModel::logout,
            onNavigateToSignOut = { navigator.navigateToSignOut(activity) },
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(KuringTheme.colors.background),
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
        Intent(Intent.ACTION_VIEW, appScheme.toUri())
    } catch (_: PackageManager.NameNotFoundException) {
        Intent(Intent.ACTION_VIEW, webScheme.toUri())
    }
}
