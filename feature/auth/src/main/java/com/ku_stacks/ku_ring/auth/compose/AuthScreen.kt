package com.ku_stacks.ku_ring.auth.compose

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ku_stacks.ku_ring.auth.compose.signin.signInNavGraph
import com.ku_stacks.ku_ring.auth.compose.signout.signOutNavGraph
import com.ku_stacks.ku_ring.auth.compose.signup.signUpNavGraph

@Composable
internal fun AuthScreen(
    onNavigateUp: () -> Unit,
    startDestination: AuthDestination,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navigationSpec = tween<IntOffset>(durationMillis = 200)

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
        modifier = modifier.fillMaxSize()
    ) {
        signInNavGraph(
            navController = navController,
            onNavigateUp = onNavigateUp,
        )

        signUpNavGraph(navController = navController)

        signOutNavGraph(
            onNavigateUp = onNavigateUp,
            navController = navController
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
    get() = AuthDestination.getOrder(destination)
