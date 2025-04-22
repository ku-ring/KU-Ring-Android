package com.ku_stacks.ku_ring.auth.compose.signout

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ku_stacks.ku_ring.auth.compose.AuthDestination
import com.ku_stacks.ku_ring.auth.compose.signout.inner_screen.SignOutCompleteScreen
import com.ku_stacks.ku_ring.auth.compose.signout.inner_screen.SignOutGuideScreen

internal fun NavGraphBuilder.signOutNavGraph(
    onNavigateUp: () -> Unit,
    navController: NavHostController,
) {
    navigation<AuthDestination.SignOut>(
        startDestination = AuthDestination.SignOutGuide,
    ) {
        composable<AuthDestination.SignOutGuide> {
            SignOutGuideScreen(
                onNavigateUp = onNavigateUp,
                onNavigateToSignOutComplete = {
                    navController.navigate(AuthDestination.SignOutComplete)
                },
            )
        }
        composable<AuthDestination.SignOutComplete> {
            SignOutCompleteScreen(
                onNavigateToMyPage = onNavigateUp,
            )
        }
    }
}
