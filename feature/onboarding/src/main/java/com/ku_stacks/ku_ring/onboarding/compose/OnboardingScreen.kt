package com.ku_stacks.ku_ring.onboarding.compose

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.onboarding.compose.inner_screen.ConfirmDepartmentScreen
import com.ku_stacks.ku_ring.onboarding.compose.inner_screen.FeatureTabs
import com.ku_stacks.ku_ring.onboarding.compose.inner_screen.OnboardingCompleteScreen
import com.ku_stacks.ku_ring.onboarding.compose.inner_screen.SetDepartmentScreen

@Composable
internal fun OnboardingScreen(
    onNavigateToMain: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val navigationSpec = tween<IntOffset>(
        durationMillis = 200,
    )

    val background = KuringTheme.colors.background
    NavHost(
        navController = navController,
        startDestination = OnboardingScreenDestinations.FEATURE_TABS,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                towards = animationDirection(initialState, targetState),
                animationSpec = navigationSpec,
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = animationDirection(initialState, targetState),
                animationSpec = navigationSpec,
            )
        }
    ) {
        onboardingNavGraph(
            navHostController = navController,
            onNavigateToMain = onNavigateToMain,
            viewModel = viewModel,
            modifier = Modifier
                .background(background)
                .fillMaxSize()
        )
    }
}

private fun NavGraphBuilder.onboardingNavGraph(
    navHostController: NavHostController,
    onNavigateToMain: () -> Unit,
    viewModel: OnboardingViewModel,
    modifier: Modifier = Modifier,
) {
    composable(OnboardingScreenDestinations.FEATURE_TABS) {
        FeatureTabs(
            onNavigateToSetDepartment = {
                navHostController.navigate(OnboardingScreenDestinations.SET_DEPARTMENT)
            },
            onSkipOnboarding = onNavigateToMain,
            modifier = modifier,
        )
    }
    composable(OnboardingScreenDestinations.SET_DEPARTMENT) {
        SetDepartmentScreen(
            viewModel = viewModel,
            onSetDepartmentComplete = {
                navHostController.navigate(OnboardingScreenDestinations.CONFIRM_DEPARTMENT)
            },
            modifier = modifier,
        )
    }
    composable(OnboardingScreenDestinations.CONFIRM_DEPARTMENT) {
        ConfirmDepartmentScreen(
            viewModel = viewModel,
            onConfirm = {
                navHostController.navigate(OnboardingScreenDestinations.ONBOARDING_COMPLETE)
            },
            onCancel = {
                navHostController.navigate(OnboardingScreenDestinations.SET_DEPARTMENT)
            },
            modifier = modifier,
        )
    }
    composable(OnboardingScreenDestinations.ONBOARDING_COMPLETE) {
        OnboardingCompleteScreen(
            onStartKuring = onNavigateToMain,
            modifier = modifier,
        )
    }
}

private fun animationDirection(initialState: NavBackStackEntry, targetState: NavBackStackEntry) =
    if (initialState.screenOrder < targetState.screenOrder) {
        AnimatedContentTransitionScope.SlideDirection.Left
    } else {
        AnimatedContentTransitionScope.SlideDirection.Right
    }

private val NavBackStackEntry.screenOrder: Int
    get() = OnboardingScreenDestinations.getOrder(this.destination.route)