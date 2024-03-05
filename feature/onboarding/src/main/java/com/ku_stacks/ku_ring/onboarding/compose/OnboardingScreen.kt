package com.ku_stacks.ku_ring.onboarding.compose

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.theme.Background
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.onboarding.compose.inner_screen.ConfirmDepartment
import com.ku_stacks.ku_ring.onboarding.compose.inner_screen.FeatureTabs
import com.ku_stacks.ku_ring.onboarding.compose.inner_screen.OnboardingComplete
import com.ku_stacks.ku_ring.onboarding.compose.inner_screen.SetDepartment

@Composable
private fun OnboardingScreen(
    onNavigateToMain: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navigationSpec = tween<IntOffset>(
        durationMillis = 200,
    )

    NavHost(
        navController = navController,
        startDestination = featureTabs,
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
        )
    }
}

private fun NavGraphBuilder.onboardingNavGraph(
    navHostController: NavHostController,
    onNavigateToMain: () -> Unit,
) {
    val modifier = Modifier
        .background(Background)
        .fillMaxSize()
    composable(featureTabs) {
        FeatureTabs(
            onNavigateToSetDepartment = {
                navHostController.navigate(setDepartment)
            },
            modifier = modifier,
        )
    }
    composable(setDepartment) {
        SetDepartment(
            onSetDepartmentComplete = {
                // TODO: 학과 이름 넘겨주기 (viewModel을 navHost에 선언?)
                navHostController.navigate(confirmDepartment)
            },
            modifier = modifier,
        )
    }
    composable(confirmDepartment) {
        ConfirmDepartment(
            onConfirm = {
                navHostController.navigate(onboardingComplete)
            },
            onCancel = {
                navHostController.navigate(setDepartment)
            },
            modifier = modifier,
        )
    }
    composable(onboardingComplete) {
        OnboardingComplete(
            onStartKuring = onNavigateToMain,
            modifier = modifier,
        )
    }
}

private const val featureTabs = "feature_tabs"
private const val setDepartment = "set_department"
private const val confirmDepartment = "confirm_department"
private const val onboardingComplete = "onboarding_complete"

private val NavBackStackEntry.route: String?
    get() = destination.route

private fun screenOrder(state: NavBackStackEntry) = when (state.route) {
    featureTabs -> 0
    setDepartment -> 1
    confirmDepartment -> 2
    onboardingComplete -> 3
    else -> 4
}

private fun animationDirection(initialState: NavBackStackEntry, targetState: NavBackStackEntry) =
    if (screenOrder(initialState) < screenOrder(targetState)) {
        AnimatedContentTransitionScope.SlideDirection.Left
    } else {
        AnimatedContentTransitionScope.SlideDirection.Right
    }

@LightAndDarkPreview
@Composable
private fun OnboardingScreenPreview() {
    KuringTheme {
        OnboardingScreen(
            onNavigateToMain = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}