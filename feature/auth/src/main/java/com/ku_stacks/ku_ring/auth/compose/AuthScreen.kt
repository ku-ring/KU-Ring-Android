package com.ku_stacks.ku_ring.auth.compose

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ku_stacks.ku_ring.auth.compose.reset_password.resetPasswordNavGraph
import com.ku_stacks.ku_ring.auth.compose.signin.signInNavGraph
import com.ku_stacks.ku_ring.auth.compose.signout.signOutNavGraph
import com.ku_stacks.ku_ring.auth.compose.signup.signUpNavGraph
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme

@Composable
internal fun AuthScreen(
    onNavigateUp: () -> Unit,
    startDestination: AuthDestination,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navigationSpec = tween<IntOffset>(durationMillis = 200)

    Scaffold(
        containerColor = KuringTheme.colors.background,
        modifier = modifier.fillMaxSize(),
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
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
            },
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues),
        ) {
            signInNavGraph(
                navController = navController,
                onNavigateUp = onNavigateUp,
            )

            signUpNavGraph(navController = navController)

            resetPasswordNavGraph(navController = navController)

            signOutNavGraph(
                onNavigateUp = onNavigateUp,
                navController = navController
            )
        }
    }
}

private fun animationDirection(initialState: NavBackStackEntry, targetState: NavBackStackEntry) =
    if (initialState.screenOrder < targetState.screenOrder) {
        AnimatedContentTransitionScope.SlideDirection.Left
    } else {
        AnimatedContentTransitionScope.SlideDirection.Right
    }

private val NavBackStackEntry.screenOrder: Int
    get() = AuthDestination.getOrder(destination)

@Composable
internal inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavHostController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}
